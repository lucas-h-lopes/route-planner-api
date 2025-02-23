package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.web.controller.dto.login.LoginDTO;
import com.study.projects.percursos_van.exception.InvalidCredentialsException;
import com.study.projects.percursos_van.exception.handler.ExceptionBody;
import com.study.projects.percursos_van.jwt.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final JwtUserDetailsService service;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<Object> authenticate(@RequestBody @Valid LoginDTO dto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            authenticationManager.authenticate(token);

            return ResponseEntity.ok()
                    .body(service.generateToken(dto.email()));
        } catch (AuthenticationException e) {
            log.info("Falha na autenticação para o usuário: {}", dto.email());
        }
        InvalidCredentialsException e = new InvalidCredentialsException("Credenciais inválidas");
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, e.getMessage(), e));
    }
}
