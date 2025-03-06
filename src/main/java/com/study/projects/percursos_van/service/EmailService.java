package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.FailedEmailCreationException;
import com.study.projects.percursos_van.model.EmailChangeToken;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailChangeTokenService emailChangeTokenService;
    private final UserRepository userRepository;
    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${url.base-url.dev}")
    private String baseUrl;

    @Value("${url.resource.confirmation}")
    private String confirmationResourceUrl;

    private MimeMessage createMimeMessage(String recipient, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setSubject("Confirmação de alteração de e-mail");
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setText(text, true);

            return mimeMessage;
        } catch (MessagingException e) {
            log.info("Falha na criação da MimeMessage: ", e);
            return null;
        }
    }

    @Transactional
    public void sendWithEmailChange(User user, String newEmail) {
        EmailChangeToken changeToken = new EmailChangeToken();
        changeToken.setToken(UUID.randomUUID().toString());
        changeToken.setUser(userRepository.findByEmail(user.getEmail()).get());
        changeToken.setNewEmail(newEmail);
        changeToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        emailChangeTokenService.insert(changeToken);

        send(newEmail, user);
    }

    @Transactional
    public void send(String recipient) {
        try {
            MimeMessage mimeMessage = createMimeMessage(recipient, "Texto padrão");
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailedEmailCreationException("Algo deu errado durante o envio do e-mail");
        }
    }

    @Transactional
    public void send(String recipient, User user) {
        try {
            String url = this.baseUrl + confirmationResourceUrl + "?token=" +
                    emailChangeTokenService.findByNearestExpiringDateList(
                            emailChangeTokenService.findByUser(user))
                            .getToken();

            MimeMessage mimeMessage = createMimeMessage(recipient, emailTemplateService.loadConfirmationHTML(user.getFullName(), url));
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailedEmailCreationException("Algo deu errado durante o envio do e-mail");
        }
    }
}
