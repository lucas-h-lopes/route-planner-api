package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.FailedEmailCreationException;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.from}")
    private String from;

    private MimeMessage createMimeMessage(String recipient, String text, String subject) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setSubject(subject);
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
    public void send(String recipient, User user, EmailTemplate template, String subject) {
        try {
            MimeMessage mimeMessage = createMimeMessage(recipient, emailTemplateService.getTemplate(template, user), subject);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailedEmailCreationException("Algo deu errado durante o envio do e-mail");
        }
    }
}
