package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.*;
import com.study.projects.percursos_van.model.*;
import com.study.projects.percursos_van.model.enums.EmailTemplate;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.repository.DriverRepository;
import com.study.projects.percursos_van.repository.UserRepository;
import com.study.projects.percursos_van.repository.projection.UserProjection;
import com.study.projects.percursos_van.repository.projection.impl.UserProjectionImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AccountEmailChangeTokenService accountEmailChangeTokenService;
    private final AccountDeletionTokenService accountDeletionTokenService;

    @Transactional
    public User insert(User user) {
        try {
            boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
            if (existsByEmail) {
                throw new DuplicatedEmailException("Já existe uma conta associada a este e-mail");
            }
            formatAndSetFullName(user);
            formatAndSetEmail(user);
            encodeAndSetPassword(user);

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedCpfException("Não é possível concluir o cadastro com o CPF informado");
        }
    }

    @Transactional
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public void requestEmailChange(String newEmail, String confirmationEmail, String actualPassword, User authenticatedUser) {
        if (!newEmail.equalsIgnoreCase(confirmationEmail)) {
            throw new MismatchedEmailException("Os e-mails não conferem");
        }

        if (!passwordEncoder.matches(actualPassword, authenticatedUser.getPassword())) {
            throw new InvalidCredentialsException("As credenciais não conferem");
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new DuplicatedEmailException("Já existe uma conta associada a este e-mail");
        }

        AccountEmailChangeToken changeToken = accountEmailChangeTokenService.prepareChangeToken(authenticatedUser, newEmail);
        accountEmailChangeTokenService.insert(changeToken);

        emailService.send(newEmail, authenticatedUser, EmailTemplate.ACCOUNT_EMAIL_CHANGE_CONFIRMATION, "Confirmação de alteração de e-mail");
    }

    @Transactional
    public void updatePassword(String currentPassword, String newPassword, String confirmationPassword, User user) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Senha atual incorreta");
        }

        if (!newPassword.equals(confirmationPassword)) {
            throw new MismatchedPasswordException("Nova senha e confirmação de senha devem ser iguais");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserProjection> findAllPageable(Pageable pageable, String name, String role) {
        User user = new User();
        user.setFullName(name);
        if (role != null) {
            UserRoleValidator.validateUserRole(role);
            user.setRole(Role.valueOf(role.toUpperCase()));
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<User> userExample = Example.of(user, matcher);

        Page<User> userPage = userRepository.findAll(userExample, pageable);

        return userPage.map(UserProjectionImpl::new);
    }

    @Transactional
    public void deleteById(Integer id) {
        User user = findById(id);

        List<AccountEmailChangeToken> allEmailChangeTokens =
                accountEmailChangeTokenService.findAllByUser(user);

        List<AccountDeletionToken> allDeletionTokens =
                accountDeletionTokenService.findAllByUser(user);

        if (!allEmailChangeTokens.isEmpty()) {
            accountEmailChangeTokenService.deleteAll(allEmailChangeTokens);
        }

        if (!allDeletionTokens.isEmpty()) {
            accountDeletionTokenService.deleteAll(allDeletionTokens);
        }

        deleteEntityByUserRole(user);

        userRepository.delete(user);
    }

    @Transactional
    public void requestAccountDeletion(User user) {
        AccountDeletionToken deletionToken = accountDeletionTokenService.prepareToken(user);
        accountDeletionTokenService.insert(deletionToken);

        emailService.send(user.getEmail(), user, EmailTemplate.ACCOUNT_DELETION_CONFIRMATION, "Confirmação de exclusão de conta");
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public void updateUser(User user){
        try {
            userRepository.save(user);
        }catch(Exception e){
            throw new FailedEntityCreationException("Algo de errado durante a atualização de usuário");
        }
    }

    @Transactional(readOnly = true)
    public Role findRoleFromEmail(String email) {
        return userRepository.findRoleByEmail(email);
    }

    private void formatAndSetFullName(User user) {
        String fullName = user.getFullName();
        user.setFullName(Arrays.stream(fullName.trim()
                        .replaceAll("\\s+", " ")
                        .split(" "))
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase())
                .collect(Collectors.joining(" ")));
    }

    private void formatAndSetEmail(User user) {
        String email = user.getEmail();
        user.setEmail(email.trim().toLowerCase());
    }

    private void encodeAndSetPassword(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
    }

    private void deleteEntityByUserRole(User user) {
        if (user.getRole().equals(Role.DRIVER)) {
            Driver driver = driverRepository.findByUser(user);
            driverRepository.delete(driver);
        }
    }

}
