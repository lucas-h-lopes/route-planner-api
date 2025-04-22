package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedPhoneNumberException;
import com.study.projects.percursos_van.exception.LimitExceedPhoneException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.exception.SinglePhoneDeletionException;
import com.study.projects.percursos_van.model.Phone;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.PhoneRepository;
import com.study.projects.percursos_van.web.mapper.phone.PhoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final UserService userService;
    private static final String errorMessage = "Não foi possível processar a solicitação para este telefone";

    @Transactional
    public Phone insert(Phone phone) {
        try {
            return phoneRepository.save(phone);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedPhoneNumberException("Este número de telefone já está em uso");
        }
    }

    private void validateUserPhonesQuantity(User user, String[] rawPhones) {
        int total = user.getPhones().size() + rawPhones.length;
        if (total > 3) {
            throw new LimitExceedPhoneException("O usuário não pode ter mais do que 3 telefones cadastrados.");
        }
    }

    private Phone[] createPhonesFromNumbers(String[] rawNumbers, User user) {
        validateUserPhonesQuantity(user, rawNumbers);
        List<Phone> phones = Arrays.stream(rawNumbers)
                .map(x -> new Phone(x, Phone.PhoneType.SECONDARY, user)).toList();
        if (user.getPhones().isEmpty()) {
            phones.getFirst().setPhoneType(Phone.PhoneType.PRINCIPAL);
        }
        return phones.toArray(new Phone[0]);
    }

    public void insertMany(String[] rawPhones, User user) {
        Phone[] phones = createPhonesFromNumbers(rawPhones, user);
        validateManyPhones(null, phones);
        phoneRepository.saveAll(
                Arrays.asList(phones)
        );
        user.addManyPhones(Arrays.asList(phones));
        userService.updateUser(user);
    }

    @Transactional
    public void update(int userId, String identifier, String newPhone) {
        User user = userService.findById(userId);
        Phone userPhone = getByIdentifier(identifier);

        throwIfNumberAlreadyExists(newPhone);

        if (!user.getPhones().contains(userPhone)) {
            throw new NotFoundException(errorMessage);
        }

        Phone requestPhone = PhoneMapper.toPrincipalPhone(newPhone, user);

        userPhone.setNumber(requestPhone.getNumber());

        phoneRepository.save(userPhone);
    }

    @Transactional
    public void insertMany(Phone phone, Phone[] phones) {
        validateManyPhones(phone.getNumber(), phones);

        phoneRepository.save(phone);
        if (phones != null) {
            phoneRepository.saveAll(Arrays.asList(phones));
        }
    }

    @Transactional
    public void throwIfNumberAlreadyExists(String number) {
        if (phoneRepository.findByNumber(number).isPresent()) {
            throw new DuplicatedPhoneNumberException("Este número de telefone já está em uso");
        }
    }

    public void validateManyPhones(String phone, Phone[] phones) {
        if (phone != null) {
            throwIfNumberAlreadyExists(phone);
        }
        if (phones != null) {
            for (Phone phoneArr : phones) {
                if (phoneArr.getNumber().equals(phone)) {
                    throw new DuplicatedPhoneNumberException("O número de telefone '" + phoneArr.getNumber() + "' não pode ser salvo como principal e secundário");
                }
                throwIfNumberAlreadyExists(phoneArr.getNumber());
            }
        }
    }

    @Transactional
    public void deleteByIdentifier(String identifier, int userId) {
        User user = userService.findById(userId);
        Phone phone = getByIdentifier(identifier);

        if (user.getPhones().size() == 1) {
            throw new SinglePhoneDeletionException("O usuário não pode excluir o único telefone associado à sua conta");
        }

        if (!user.getPhones().contains(phone)) {
            throw new NotFoundException(errorMessage);
        }

        phoneRepository.delete(phone);
        user.getPhones().remove(phone);
        Phone newFirstPhone = user.getPhones().getFirst();
        if (!newFirstPhone.getPhoneType().equals(Phone.PhoneType.PRINCIPAL)) {
            newFirstPhone.setPhoneType(Phone.PhoneType.PRINCIPAL);
            phoneRepository.save(newFirstPhone);
        }
        userService.updateUser(user);
    }

    @Transactional(readOnly = true)
    public Phone getByIdentifier(String identifier) {
        if (!isPhoneNumberFormat(identifier)) {
            return phoneRepository.findById(new BigInteger(identifier).intValue())
                    .orElseThrow(
                            () -> new NotFoundException(errorMessage)
                    );
        }
        return phoneRepository
                .findByNumber(identifier)
                .orElseThrow(
                        () -> new NotFoundException(errorMessage)
                );
    }

    private boolean isPhoneNumberFormat(String identifier) {
        return identifier
                .trim()
                .replaceAll("\\s", "")
                .length() == 11;
    }

    public Phone[] getOtherPhones(User user) {
        if (user.getPhones().size() > 1) {
            return user.getPhones()
                    .subList(1, user.getPhones().size())
                    .toArray(new Phone[0]);
        }
        return null;
    }
}
