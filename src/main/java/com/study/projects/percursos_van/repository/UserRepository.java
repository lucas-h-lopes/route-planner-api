package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("select u.role from User u where u.email = :email")
    Role findRoleByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
