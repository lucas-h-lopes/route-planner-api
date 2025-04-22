package com.study.projects.percursos_van.web.controller.dto.phone;

import com.study.projects.percursos_van.validation.anotation.PhoneArray;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PhoneArrayCreateDTO(
        @Size(min = 1, max = 3, message = "É necessário informar entre 1 a 3 telefones")
        @NotEmpty(message = "É necessário informar ao menos um telefone")
        @PhoneArray
        String[] phones
) {
}
