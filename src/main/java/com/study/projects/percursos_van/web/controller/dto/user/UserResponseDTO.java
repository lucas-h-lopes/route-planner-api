package com.study.projects.percursos_van.web.controller.dto.user;

import com.study.projects.percursos_van.model.User;

public record UserResponseDTO(Integer id, String fullName, String email, String role) {

    public UserResponseDTO(User user){
        this(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
