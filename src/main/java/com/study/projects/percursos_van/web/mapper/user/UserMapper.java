package com.study.projects.percursos_van.web.mapper.user;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.web.controller.dto.user.UserCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserResponseDTO;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class UserMapper {

    public static User toUser(UserCreateDTO dto, User user) {
        User mappedUser = new User(dto);
        mappedUser.setRole(Role.ADMIN);
        return mappedUser;
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user);
    }

    public static List<UserResponseDTO> toResponseList(List<User> users) {
        return users.stream().map(UserMapper::toResponse).toList();
    }
}
