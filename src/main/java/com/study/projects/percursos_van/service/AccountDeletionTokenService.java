package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedTokenException;
import com.study.projects.percursos_van.exception.InvalidTokenException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.AccountDeletionToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.AccountDeletionTokenRepository;
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
public class AccountDeletionTokenService {

    private final AccountDeletionTokenRepository repository;

    public AccountDeletionToken insert(AccountDeletionToken token){
        try{
            return repository.save(token);
        }catch(DataIntegrityViolationException e){
            throw new DuplicatedTokenException("Não foi possível gerar o token");
        }
    }

    public AccountDeletionToken prepareToken(User user){
        AccountDeletionToken token = new AccountDeletionToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        return token;
    }

    @Transactional(readOnly = true)
    public AccountDeletionToken findByNearestExpirationDate(User user){
        return findAllByUser(user)
                .stream().filter(x -> x.getExpiresAt().isAfter(LocalDateTime.now()))
                .max(Comparator.comparing(AccountDeletionToken::getExpiresAt))
                .orElseThrow(() -> new NotFoundException("Nenhum token válido foi encontrado"));
    }

    @Transactional(readOnly = true)
    public List<AccountDeletionToken> findAllByUser(User user){
        return repository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public AccountDeletionToken findByToken(String token){
        return repository.findByToken(token).orElseThrow(
                () -> new InvalidTokenException("O token informado é inválido")
        );
    }

    @Transactional
    public void validateToken(AccountDeletionToken token){
        token.validateToken();
    }

    public void deleteAll(List<AccountDeletionToken> allTokens) {
        if (allTokens == null || allTokens.isEmpty()) {
            throw new NotFoundException("Nenhum token foi encontrado para exclusão.");
        }

        repository.deleteAll(allTokens);
    }
}

