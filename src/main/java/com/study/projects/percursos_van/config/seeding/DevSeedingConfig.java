package com.study.projects.percursos_van.config.seeding;

import com.study.projects.percursos_van.builder.AddressBuilder;
import com.study.projects.percursos_van.model.*;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.service.AddressService;
import com.study.projects.percursos_van.service.DriverService;
import com.study.projects.percursos_van.service.StudentService;
import com.study.projects.percursos_van.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Profile("dev")
@Configuration
@DependsOn("prodSeedingConfig")
@RequiredArgsConstructor
public class DevSeedingConfig {

    private final Random random;
    private final UserService userService;
    private final DriverService driverService;
    private final StudentService studentService;

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
            user.setCreatedAt(LocalDateTime.now());
            user.setLastModifiedAt(LocalDateTime.now());
            user.setCpf(randomNumbers(11));
            setCreatedAndModified(user);

            switch (user.getRole()) {
                case Role.DRIVER -> {
                    Driver driver = new Driver();
                    user.setPhones(
                            Collections.singletonList(new Phone(randomNumbers(11), Phone.PhoneType.PRINCIPAL, user))
                    );
                    setCreatedAndModified(user.getPhones().getFirst());
                    driver.setUser(user);
                    setCreatedAndModified(driver);
                    driverService.insert(driver);
                }
                case Role.STUDENT -> {
                    Student student = new Student();
                    Address address = AddressBuilder.withDefaultAddress().get();
                    address.setStudent(student);

                    student.getAddresses().add(address);

                    setCreatedAndModified(student);

                    student.setUser(user);
                    studentService.insert(student);
                }
                default -> userService.insert(user);
            }
        }
    }

    private String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder rawPhone = new StringBuilder();
        for (int x = 0; x < 10; x++) {
            rawPhone.append(randomNumbers(11));
        }
        return rawPhone.toString();
    }

    private String randomNumbers(int times) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < times; x++) {
            sb.append(random.nextInt(0, 10));
        }
        return sb.toString();
    }

    private void setCreatedAndModified(Driver driver) {
        driver.setLastModifiedBy("System");
        driver.setCreatedBy("System");
    }

    private void setCreatedAndModified(Student student) {
        student.setLastModifiedBy("System");
        student.setCreatedBy("System");

        if (!student.getAddresses().isEmpty()) {
            for (Address address : student.getAddresses()) {
                address.setLastModifiedBy("System");
                address.setCreatedBy("System");
            }
        }
    }

    private void setCreatedAndModified(User user) {
            user.setLastModifiedBy("System");
            user.setCreatedBy("System");
    }

    private void setCreatedAndModified(Phone phone){
            phone.setCreatedBy("System");
            phone.setLastModifiedBy("System");
    }
}
