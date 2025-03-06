package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.InvalidRoleException;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public class UserRoleValidator {

    private final static List<String> validRoles = List.of("ADMIN", "DRIVER", "STUDENT");

    private UserRoleValidator() {
    }

    public static void validateUserRole(String role, User user) {
        try {
            Role.valueOf(role.toUpperCase());
            if (user.getRole().name().equalsIgnoreCase(Role.ADMIN.name()) && role.equalsIgnoreCase("STUDENT")) {
                throw new AccessDeniedException("Somente usuários com papél 'DRIVER' podem criar usuários com papel 'STUDENT'");
            }
            if (user.getRole().name().equalsIgnoreCase(Role.DRIVER.name()) && !role.equalsIgnoreCase("STUDENT")) {
                throw new AccessDeniedException("Usuários com papél 'DRIVER' só podem criar usuários com papel 'STUDENT'");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException(String.format("O papel '%s' não válido. Papéis válidos: %s", role,
                    String.join(", ", validRoles)));
        }
    }

    public static void validateUserRole(String role){
        try{
            Role.valueOf(role.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new InvalidRoleException(String.format("O papel '%s' não válido. Papéis válidos: %s", role,
                    String.join(", ", validRoles)));
        }
    }
}
