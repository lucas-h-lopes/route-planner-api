package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedTokenException;
import com.study.projects.percursos_van.exception.InvalidTokenException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.AccountEmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.AccountEmailChangeTokenRepository;
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
public class AccountEmailChangeTokenService {

    private final AccountEmailChangeTokenRepository repository;

    @Transactional
    public AccountEmailChangeToken insert(AccountEmailChangeToken entity){
        try{
            return repository.save(entity);
        }catch (DataIntegrityViolationException e){
            throw new DuplicatedTokenException("Não foi possível gerar o token");
        }
    }

    public AccountEmailChangeToken prepareChangeToken(User user, String newEmail){
        AccountEmailChangeToken changeToken = new AccountEmailChangeToken();
        changeToken.setToken(UUID.randomUUID().toString());
        changeToken.setUser(user);
        changeToken.setNewEmail(newEmail);
        changeToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        return changeToken;
    }

    @Transactional(readOnly = true)
    public AccountEmailChangeToken findByToken(String token){
        return repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("O token informado é inválido"));
    }

    @Transactional(readOnly = true)
    public List<AccountEmailChangeToken> findAllByUser(User user){
        return repository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public AccountEmailChangeToken findByNearestExpiringDate(User user){
        return findAllByUser(user)
                .stream()
                .filter(x -> x.getExpiresAt().isAfter(LocalDateTime.now()))
                .max(Comparator.comparing(AccountEmailChangeToken::getExpiresAt))
                .orElseThrow(() -> new NotFoundException("Nenhum token válido foi encontrado"));
    }

    public void validateToken(AccountEmailChangeToken token){
        token.validateToken();
    }

    @Transactional
    public void deleteAll(List<AccountEmailChangeToken> changeTokenList){
        try {
            repository.deleteAll(changeTokenList);
        }catch(Exception e){
            throw new NotFoundException("Nenhum token válido foi encontrado");
        }
    }
}
