package com.study.projects.percursos_van.web.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.web.mapper.phone.PhoneMapper;

public record UserResponseDTO(Integer id, String fullName, String cpf, String email, String role, @JsonInclude(JsonInclude.Include.NON_NULL) String[] phones) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getFullName(), user.getCpf(), user.getEmail(), user.getRole().name(), PhoneMapper.getNumbersFromPhonesArr(user.getPhones()));
    }
}
