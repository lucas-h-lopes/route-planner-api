package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.model.EmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.service.EmailChangeTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmailChangeController {

    private final EmailChangeTokenService service;

    @GetMapping("/confirmation")
    public ResponseEntity<String> confirmEmailChange(@RequestParam("token") String token){
        EmailChangeToken changeToken = service.findByToken(token);
        User user = changeToken.getUser();

        service.changeUserEmail(user, changeToken);

        return ResponseEntity.ok("Email atualizado com sucesso!");
    }
}
