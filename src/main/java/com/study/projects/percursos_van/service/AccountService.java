package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.InvalidTokenException;
import com.study.projects.percursos_van.model.AccountDeletionToken;
import com.study.projects.percursos_van.model.AccountEmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDeletionTokenService accountDeletionTokenService;
    private final AccountEmailChangeTokenService accountEmailChangeTokenService;
    private final UserRepository userRepository;
    private final DriverService driverService;

    @Transactional
    public void updateUserEmail(User user, AccountEmailChangeToken changeToken) {
        if (!changeToken.equals(accountEmailChangeTokenService.findByNearestExpiringDate(user))) {
            throw new InvalidTokenException("Token inválido ou expirado");
        }
        accountEmailChangeTokenService.validateToken(changeToken);
        user.setEmail(changeToken.getNewEmail());
        userRepository.save(user);
        List<AccountEmailChangeToken> allChangeTokens = accountEmailChangeTokenService.findAllByUser(user);

        accountEmailChangeTokenService.deleteAll(allChangeTokens);
    }

    @Transactional
    public void deleteUser(User user, AccountDeletionToken deletionToken) {
        if (!deletionToken.equals(accountDeletionTokenService.findByNearestExpirationDate(user))) {
            throw new InvalidTokenException("Token inválido ou expirado");
        }
        accountDeletionTokenService.validateToken(deletionToken);

        List<AccountDeletionToken> allTokens = accountDeletionTokenService.findAllByUser(user);
        if (!allTokens.isEmpty()) {
            accountDeletionTokenService.deleteAll(allTokens);
        }

        List<AccountEmailChangeToken> allChangeTokens = accountEmailChangeTokenService.findAllByUser(user);
        if (!allChangeTokens.isEmpty()) {
            accountEmailChangeTokenService.deleteAll(allChangeTokens);
        }

        if (user.getRole().equals(Role.DRIVER)) {
            driverService.deleteByUser(user);
        }

        userRepository.delete(user);
    }
}
