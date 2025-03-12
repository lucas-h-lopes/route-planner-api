package com.study.projects.percursos_van.config.seeding;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Random;

@Profile("dev")
@Configuration
@DependsOn("prodSeedingConfig")
@RequiredArgsConstructor
public class DevSeedingConfig {

    private final Random random;
    private final UserService userService;
    private final String[] fullNamesArr = new String[]{
            "Lucas Henrique Lopes",
            "Anthony Miguel Samuel da Cruz",
            "Otávio Pedro Henrique Porto",
            "Fábio Noah Yuri Viana",
            "Enzo Guilherme Cardoso",
            "Emilly Raimunda Rayssa Caldeira",
            "Mateus Caio da Luz",
            "Renata Helena Almada",
            "Gael Márcio Aparício",
            "Manuel Sebastião Mendes",
            "Evelyn Vera Bianca Farias",
            "Ian Pedro André Baptista",
            "Miguel Leandro Theo Souza",
            "Esther Elaine Galvão",
            "Lucca Ricardo Barros"
    };

    private final Role[] validRoles = new Role[]{
            Role.ADMIN,
            Role.STUDENT,
            Role.DRIVER
    };

    @PostConstruct
    public void insertData() {
        for (int x = 0; x < 30; x++) {
            User user = new User();
            user.setRole(validRoles[random.nextInt(0, 3)]);
            user.setFullName(fullNamesArr[random.nextInt(0, 15)]);
            user.setEmail("testeseeding" + (x + 1) + "@email.com");
            user.setPassword("12345678aA@");
            user.setCreatedBy("System");
            user.setCreatedAt(LocalDateTime.now());
            user.setLastModifiedBy("System");
            user.setLastModifiedAt(LocalDateTime.now());
            user.setCpf(randomNumbers());

            userService.insert(user);
        }
    }

    private String randomNumbers(){
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x<11; x++){
            sb.append(random.nextInt(0,10));
        }
        return sb.toString();
    }
}
