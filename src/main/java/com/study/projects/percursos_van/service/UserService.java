package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedEmailException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User insert(User user) {
        try {
            formatAndSetFullName(user);
            formatAndSetEmail(user);
            encodeAndSetPassword(user);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedEmailException("Usuário '" + user.getEmail() + "' já está cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário '" + email + "' não foi encontrado no sistema"));
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
}
