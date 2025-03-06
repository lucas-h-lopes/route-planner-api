package com.study.projects.percursos_van.web.controller.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateEmailDTO(
        @NotBlank(message = "O novo email é obrigatório")
        @Pattern(regexp = "^[a-zA-Z0-9]+([.-_][a-zA-Z0-9]+)*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O novo email deve seguir o seguinte padrão: lucas@email.com")
        String newEmail,
        @NotBlank(message = "A confirmação do novo email é obrigatória")
        @Pattern(regexp = "^[a-zA-Z0-9]+([.-_][a-zA-Z0-9]+)*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "A confirmação do novo email deve seguir o seguinte padrão: lucas@email.com")
        String confirmationEmail,
        @NotBlank(message = "A senha é obrigatória")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais")
        String password) {
}
