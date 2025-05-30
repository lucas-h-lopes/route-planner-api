package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    @Value("${url.base}")
    private String baseUrl;

    @Value("${url.resource.account.confirm}")
    private String confirmationResourceUrl;

    @Value("${url.resource.account.delete}")
    private String deleteConfirmationResourceUrl;

    private final AccountEmailChangeTokenService accountEmailChangeTokenService;
    private final AccountDeletionTokenService accountDeletionTokenService;

    public String getTemplate(EmailTemplate template, User user) {
        if (template.equals(EmailTemplate.ACCOUNT_EMAIL_CHANGE_CONFIRMATION)) {
            return loadConfirmationHTML(user);
        } else if (template.equals(EmailTemplate.ACCOUNT_DELETION_CONFIRMATION)) {
            return loadDeleteConfirmationHTML(user);
        }
        return "Template informado não existe";
    }

    private String loadConfirmationHTML(User user) {
        String url = this.baseUrl + confirmationResourceUrl + "?token=" +
                accountEmailChangeTokenService.findByNearestExpiringDate(user)
                        .getToken();

        return String.format("""
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #FCFCFC;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #FCFCFC;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                        }
                        .header {
                            background: #004080;
                            color: #ffffff;
                            padding: 15px;
                            font-size: 20px;
                            font-weight: bold;
                            border-radius: 8px 8px 0 0;
                        }
                        .content {
                            padding: 20px;
                            font-size: 16px;
                            color: #333333;
                        }
                        .button {
                            display: inline-block;
                            background: #004080;
                            color: #f0f0f0 !important;
                            padding: 10px 20px;
                            text-decoration: none;
                            font-size: 16px;
                            font-weight: bold;
                            border-radius: 5px;
                            margin-top: 15px;
                        }
                        .button:visited, .button:hover, .button:active {
                            color: #f0f0f0 !important;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 12px;
                            color: #777777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">Confirmação de Troca de E-mail</div>
                        <div class="content">
                            <p>Olá <b>%s</b>,</p>
                            <p>Recebemos uma solicitação para alterar o seu endereço de e-mail. Para confirmar essa alteração, clique no botão abaixo:</p>
                            <a href="%s" class="button"><span>Confirmar E-mail</span></a>
                            <p>Se você não solicitou essa alteração, ignore este e-mail.</p>
                            <p><strong>Este link expirará em 30 minutos.</strong></p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2025 VanGo. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """, user.getFullName().split(" ")[0], url);
    }

    private String loadDeleteConfirmationHTML(User user) {
        String url = this.baseUrl + this.deleteConfirmationResourceUrl + "?token=" + accountDeletionTokenService.findByNearestExpirationDate(user)
                .getToken();

        return String.format("""
        <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #FCFCFC;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background: #FCFCFC;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                        }
                        .header {
                            background: #FF4C4C;
                            color: #ffffff;
                            padding: 15px;
                            font-size: 20px;
                            font-weight: bold;
                            border-radius: 8px 8px 0 0;
                        }
                        .content {
                            padding: 20px;
                            font-size: 16px;
                            color: #333333;
                        }
                        .button {
                            display: inline-block;
                            background: #FF4C4C;
                            color: #f0f0f0 !important;
                            padding: 10px 20px;
                            text-decoration: none;
                            font-size: 16px;
                            font-weight: bold;
                            border-radius: 5px;
                            margin-top: 15px;
                        }
                        .button:visited, .button:hover, .button:active {
                            color: #f0f0f0 !important;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 12px;
                            color: #777777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">Confirmação de Exclusão de Conta</div>
                        <div class="content">
                            <p>Olá <b>%s</b>,</p>
                            <p>Recebemos uma solicitação para excluir a sua conta. Para confirmar essa exclusão, clique no botão abaixo:</p>
                            <a href="%s" class="button"><span>Confirmar Exclusão</span></a>
                            <p>Se você não solicitou essa exclusão, ignore este e-mail.</p>
                            <p><strong>Este link expirará em 15 minutos.</strong></p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2025 VanGo. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>""", user.getFullName().split(" ")[0], url);
    }
}
