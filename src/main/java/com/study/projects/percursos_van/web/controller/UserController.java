package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.jwt.JwtUserDetails;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.projection.UserProjection;
import com.study.projects.percursos_van.service.UserService;
import com.study.projects.percursos_van.web.controller.dto.pageable.PageableDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserPasswordDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserResponseDTO;
import com.study.projects.percursos_van.web.controller.dto.user.UserUpdateEmailDTO;
import com.study.projects.percursos_van.web.mapper.page.PageableMapper;
import com.study.projects.percursos_van.web.mapper.user.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DRIVER')")
    public ResponseEntity<UserResponseDTO> insert(@RequestBody @Valid UserCreateDTO dto, @AuthenticationPrincipal JwtUserDetails details) {
        User authenticatedUser = userService.findById(details.getId());
        User user = UserMapper.toUser(dto, authenticatedUser);
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
    public ResponseEntity<PageableDTO> findAll(@PageableDefault(size = 5, sort = "id") Pageable pageable,
                                               @RequestParam(required = false, name = "fname") String fullName,@RequestParam(required = false, name = "role") String role) {
        Page<UserProjection> projection = userService.findAllPageable(pageable, fullName, role);

        return ResponseEntity.ok(
                PageableMapper.toPageableDTO(projection));
    }

    @GetMapping("/personal-info")
    @PreAuthorize("hasAnyAuthority('DRIVER', 'STUDENT', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getByAuthenticated(@AuthenticationPrincipal JwtUserDetails details) {
        User user = userService.findById(details.getId());
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PutMapping("/personal-info/email")
    @PreAuthorize("hasAnyAuthority('DRIVER', 'STUDENT', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updateEmail(@RequestBody @Valid UserUpdateEmailDTO dto, @AuthenticationPrincipal JwtUserDetails details) {
        User authenticatedUser = userService.findById(details.getId());
        userService.requestEmailChange(dto.newEmail(), dto.confirmationEmail(), dto.password(), authenticatedUser);
        return ResponseEntity
                .ok(Map.of("message", "Link de confirmação enviado para o seu novo e-mail"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/personal-info")
    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT','DRIVER')")
    public ResponseEntity<Map<String,String>> deleteByAuthenticated(@AuthenticationPrincipal JwtUserDetails details){
        User user = userService.findById(details.getId());
        userService.requestAccountDeletion(user);
        return ResponseEntity.ok(
                Map.of("message", "Link de confirmação de exclusão da conta foi enviado para o seu e-mail"));
    }

    @PutMapping("/personal-info")
    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT','DRIVER')")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UserPasswordDTO requestBody, @AuthenticationPrincipal JwtUserDetails details){
        User authenticatedUser = userService.findById(details.getId());
        userService.updatePassword(requestBody.currentPassword(), requestBody.newPassword(), requestBody.confirmationPassword(), authenticatedUser);
        return ResponseEntity
                .noContent().build();
    }

}
