package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.jwt.JwtUserDetails;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.service.UserService;
import com.study.projects.percursos_van.web.controller.dto.user.UserCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserResponseDTO;
import com.study.projects.percursos_van.web.mapper.user.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DRIVER')")
    public ResponseEntity<UserResponseDTO> insert(@RequestBody @Valid UserCreateDTO dto, @AuthenticationPrincipal JwtUserDetails details) {
        User user = UserMapper.toUser(dto, details);
        userService.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(UserMapper.toResponse(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> findAll(@RequestParam(required = false, name = "fullname") String fullName, @RequestParam(required = false, name = "role") String role){
        List<User> users = userService.findAll(fullName, role);
        return ResponseEntity.ok(UserMapper.toResponseList(users));
    }
}
