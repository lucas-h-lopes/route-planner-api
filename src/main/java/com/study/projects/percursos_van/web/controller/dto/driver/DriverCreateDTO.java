package com.study.projects.percursos_van.web.controller.dto.driver;

import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.validation.anotation.PhoneArray;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record DriverCreateDTO(
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
        @NotBlank(message = "O CPF é obrigatório")
        @Size(min = 11, max = 11, message = "O CPF deve conter exatamente 11 dígitos (sem pontuação)")
        @CPF
        String cpf,
        @NotBlank(message = "A CNH é obrigatória")
        @Size(min = 11, max = 11, message = "A CNH precisa conter exatamente 11 dígitos")
        String cnh,
        @NotBlank(message = "A categoria da CNH é obrigatória")
        @Size(min = 1, max = 1, message = "A categoria precisa conter somente 1 caractere")
        String cnhCat,
        @NotNull(message = "A data de expiração da CNH é obrigatória")
        @FutureOrPresent(message = "A CNH precisa estar dentro da validade")
        LocalDate cnhExpiration,
        @NotBlank(message = "O telefone é obrigatório")
        @Size(min = 11, max = 11, message = "O telefone deve possuir 11 caracteres (DDD + NÚMERO)")
        String phone,
        @Size(max = 2, message = "Só é possível adicionar 2 telefones adicionais")
        @PhoneArray
        String[] otherPhones
) {

    public Driver toDriver() {
        Driver driver = new Driver();
        driver.setCnh(this.cnh);
        driver.setCnhExpiration(this.cnhExpiration);
        driver.setCnhCat(this.cnhCat.charAt(0));
        return driver;
    }
}
