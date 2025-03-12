package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.*;
import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.model.EmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.EmailTemplate;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.repository.DriverRepository;
import com.study.projects.percursos_van.repository.UserRepository;
import com.study.projects.percursos_van.repository.projection.UserProjection;
import com.study.projects.percursos_van.repository.projection.UserProjectionImpl;
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
    private final EmailChangeTokenService emailChangeTokenService;

    @Transactional
    public User insert(User user) {
        try {
            boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
            if (existsByEmail) {
                throw new DuplicatedEmailException("Usuário '" + user.getEmail() + "' já está cadastrado no sistema");
            }
            formatAndSetFullName(user);
            formatAndSetEmail(user);
            encodeAndSetPassword(user);

            insertEntityByUserRole(user);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new DuplicatedCpfException("CPF '" + user.getCpf() + "' já está cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public User findById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Usuário com id '%d' não foi encontrado no sistema", id)));
    }

    @Transactional(readOnly = true)
    public List<User> findAll(String fullName, String role){
        User user = new User();
        if(role != null) {
            UserRoleValidator.validateUserRole(role);
            user.setRole(Role.valueOf(role.toUpperCase()));
        }
        user.setFullName(fullName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<User> example = Example.of(user, matcher);
        return userRepository.findAll(example);
    }


    @Transactional
    public void updateEmail(String newEmail, String confirmationEmail, String actualPassword, User authenticatedUser){
        if(!newEmail.equalsIgnoreCase(confirmationEmail)){
            throw new MismatchedEmailException("Os e-mails não conferem");
        }

        if(!passwordEncoder.matches(actualPassword, authenticatedUser.getPassword())){
            throw new InvalidCredentialsException("As credenciais não conferem");
        }

        if(userRepository.existsByEmail(newEmail)){
            throw new DuplicatedEmailException("Já existe um usuário com este e-mail");
        }

        EmailChangeToken changeToken = emailChangeTokenService.prepareChangeToken(authenticatedUser, newEmail);
        emailChangeTokenService.insert(changeToken);

        emailService.send(newEmail, authenticatedUser, EmailTemplate.CHANGE_EMAIL);
    }

    @Transactional(readOnly = true)
    public Page<UserProjection> findAllPageable(Pageable pageable, String name, String role){
        User user = new User();
        user.setFullName(name);
        if(role != null){
            UserRoleValidator.validateUserRole(role);
            user.setRole(Role.valueOf(role.toUpperCase()));
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<User> userExample = Example.of(user,matcher);

        Page<User> userPage = userRepository.findAll(userExample, pageable);

        return userPage.map(UserProjectionImpl::new);
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

    private void insertEntityByUserRole(User user) {
        if (user.getRole().equals(Role.DRIVER)) {
            Driver driver = new Driver();
            driver.setUser(user);
            setToBlank(driver);
            driverRepository.save(driver);
        }
    }

    private void setToBlank(Driver driver) {
        driver.setCnhCat(' ');
        driver.setCnhExpiration(null);
    }
}
