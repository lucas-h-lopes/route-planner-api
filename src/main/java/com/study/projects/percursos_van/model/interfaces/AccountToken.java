package com.study.projects.percursos_van.model.interfaces;

import com.study.projects.percursos_van.exception.ExpiredTokenException;

import java.time.LocalDateTime;

public interface AccountToken {

    LocalDateTime getExpirationDate();

    default void validateToken() {
        if (getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException("O token está expirado, solicite um novo token para prosseguir");
        }
    }
}
