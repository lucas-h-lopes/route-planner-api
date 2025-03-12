package com.study.projects.percursos_van.config.seeding;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.repository.UserRepository;
import com.study.projects.percursos_van.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod", "dev"})
@RequiredArgsConstructor
public class ProdSeedingConfig {


    private final UserRepository userRepository;
    private final UserService userService;
    private final String email = "admin@email.com";

    @PostConstruct
    private void insertAdmin() {
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345678aA@");
        user.setRole(Role.ADMIN);
        user.setFullName("user administrator");
        user.setCreatedBy("System");
        user.setLastModifiedBy("System");
        user.setCpf("83334864014");

        if (!userRepository.existsByEmail(email)) {
            userService.insert(user);
        }
    }

}
