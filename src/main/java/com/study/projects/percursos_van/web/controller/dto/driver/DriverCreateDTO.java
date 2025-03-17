package com.study.projects.percursos_van.web.controller.dto.driver;

import com.study.projects.percursos_van.model.Driver;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DriverCreateDTO(
        @NotBlank(message = "A CNH é obrigatória")
        @Size(min = 11, max = 11, message = "A CNH precisa conter exatamente 11 dígitos")
        String cnh,
        @NotBlank(message = "A categoria da CNH é obrigatória")
        @Size(min = 1, max = 1, message = "A categoria precisa conter somente 1 caractere")
        String cnhCat,
        @NotNull(message = "A data de expiração da CNH é obrigatória")
        @FutureOrPresent(message = "A CNH precisa estar dentro da validade")
        LocalDate cnhExpiration
) {

    public Driver toDriver() {
        Driver driver = new Driver();
        driver.setCnh(this.cnh);
        driver.setCnhExpiration(this.cnhExpiration);
        driver.setCnhCat(this.cnhCat.charAt(0));
        return driver;
    }
}
