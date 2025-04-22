package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.InvalidRoleException;
import com.study.projects.percursos_van.model.enums.Role;

import java.util.List;

public class UserRoleValidator {

    private final static List<String> validRoles = List.of("ADMIN", "DRIVER", "STUDENT");

    private UserRoleValidator() {
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
