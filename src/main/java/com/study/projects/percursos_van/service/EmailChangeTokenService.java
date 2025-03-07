package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedTokenException;
import com.study.projects.percursos_van.exception.ExpiredTokenException;
import com.study.projects.percursos_van.exception.InvalidTokenException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.EmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.EmailChangeTokenRepository;
import com.study.projects.percursos_van.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailChangeTokenService {

    private final EmailChangeTokenRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public EmailChangeToken insert(EmailChangeToken entity){
        try{
            return repository.save(entity);
        }catch (DataIntegrityViolationException e){
            throw new DuplicatedTokenException("Token gerado já cadastrado no sistema");
        }
    }

    public EmailChangeToken prepareChangeToken(User user, String newEmail){
        EmailChangeToken changeToken = new EmailChangeToken();
        changeToken.setToken(UUID.randomUUID().toString());
        changeToken.setUser(userRepository.findByEmail(user.getEmail()).get());
        changeToken.setNewEmail(newEmail);
        changeToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        return changeToken;
    }

    @Transactional
    public void changeUserEmail(User user, EmailChangeToken changeToken){
        isTokenValid(changeToken);
        user.setEmail(changeToken.getNewEmail());
        userRepository.save(user);
        List<EmailChangeToken> allChangeTokens = findByUser(user);

        deleteAll(allChangeTokens);
    }

    @Transactional(readOnly = true)
    public EmailChangeToken findByToken(String token){
        return repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(String.format("O token informado '%s'é inválido", token)));
    }

    @Transactional(readOnly = true)
    public List<EmailChangeToken> findByUser(User user){
        return repository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public EmailChangeToken findByNearestExpiringDateList(List<EmailChangeToken> changeTokenList){
        return changeTokenList
                .stream()
                .filter(x -> x.getExpiresAt().isAfter(LocalDateTime.now()))
                .max(Comparator.comparing(EmailChangeToken::getExpiresAt))
                .orElseThrow(() -> new NotFoundException("Não foram encontrados tokens para o usuário"));
    }

    @Transactional(readOnly = true)
    public boolean isTokenValid(EmailChangeToken changeToken){
        if(changeToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("O token está expirado, solicite um novo token para prosseguir");
        }
        return true;
    }

    @Transactional
    public void deleteAll(List<EmailChangeToken> changeTokenList){
        repository.deleteAll(changeTokenList);
    }
}
