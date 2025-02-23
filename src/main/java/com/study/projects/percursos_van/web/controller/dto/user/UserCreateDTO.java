package com.study.projects.percursos_van.web.controller.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "O nome completo é obrigatório")
        @Size(min = 8, max = 50, message = "O nome completo deve possuir no mínimo 8 caracteres e no máximo 50 caracteres")
        String fullName,
        @NotBlank(message = "O email é obrigatório")
        @Pattern(regexp = "^[a-zA-Z0-9]+([.-_][a-zA-Z0-9]+)*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O email deve seguir o seguinte padrão: lucas@email.com")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais")
        String password,
        @NotBlank(message = "O papel é obrigatório")
        String role) {
}
