package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.AccountDeletionToken;
import com.study.projects.percursos_van.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountDeletionTokenRepository extends JpaRepository<AccountDeletionToken, Integer> {

    List<AccountDeletionToken> findByUser(User user);
    Optional<AccountDeletionToken> findByToken(String token);
}
