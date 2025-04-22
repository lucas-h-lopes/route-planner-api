package com.study.projects.percursos_van.web.controller.dto.phone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneCreateDTO(
        @Size(min = 11, max = 11, message = "O telefone deve possuir exatamente 11 caracteres")
        @NotBlank(message = "O telefone é obrigatório")
        String phone) {
}
