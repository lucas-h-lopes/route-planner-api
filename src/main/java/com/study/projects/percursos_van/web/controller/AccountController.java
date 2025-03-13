package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.model.AccountDeletionToken;
import com.study.projects.percursos_van.model.AccountEmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.service.AccountDeletionTokenService;
import com.study.projects.percursos_van.service.AccountEmailChangeTokenService;
import com.study.projects.percursos_van.service.AccountService;
import com.study.projects.percursos_van.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountEmailChangeTokenService emailChangeTokenService;
    private final AccountDeletionTokenService deletionTokenService;
    private final AccountService accountService;
    private final UserService userService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/email-confirmation")
    public ResponseEntity<String> confirmEmailChange(@RequestParam("token") String token) {
        AccountEmailChangeToken changeToken = emailChangeTokenService.findByToken(token);
        User user = changeToken.getUser();

        accountService.updateUserEmail(user, changeToken);

        return ResponseEntity.ok("Email atualizado com sucesso!");
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.DELETE}, value = "/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam("token") String token) {
        AccountDeletionToken deleteToken = deletionTokenService.findByToken(token);
        User user = deleteToken.getUser();

        accountService.deleteUser(user, deleteToken);
        return ResponseEntity.ok("Conta excluída com sucesso!");
    }
}
