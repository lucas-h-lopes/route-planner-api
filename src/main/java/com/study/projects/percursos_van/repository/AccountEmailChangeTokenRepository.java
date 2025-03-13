package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.AccountEmailChangeToken;
import com.study.projects.percursos_van.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountEmailChangeTokenRepository extends JpaRepository<AccountEmailChangeToken, Integer> {

    Optional<AccountEmailChangeToken> findByToken(String token);
    List<AccountEmailChangeToken> findByUser(User user);
}
