package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.service.UserService;
import com.study.projects.percursos_van.web.controller.dto.user.UserCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserResponseDTO;
import com.study.projects.percursos_van.web.mapper.user.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@RequestBody @Valid UserCreateDTO dto) {
        User user = UserMapper.toUser(dto);
        userService.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(UserMapper.toResponse(user));
    }
}
