package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.EmailChangeToken;
import com.study.projects.percursos_van.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailChangeTokenRepository extends JpaRepository<EmailChangeToken, Integer> {

    Optional<EmailChangeToken> findByToken(String token);
    List<EmailChangeToken> findByUser(User user);
}
