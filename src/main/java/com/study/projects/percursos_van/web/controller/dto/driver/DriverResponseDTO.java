package com.study.projects.percursos_van.web.controller.dto.driver;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.projects.percursos_van.model.Driver;

import java.time.LocalDate;

public record DriverResponseDTO(
        Integer id,
        String cnh,
        Character cnhCat,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate cnhExpiration,
        Integer userId
) {

    public static DriverResponseDTO toDriverResponseDTO(Driver driver){
        return new DriverResponseDTO(
                driver.getId(),
                driver.getCnh(),
                driver.getCnhCat(),
                driver.getCnhExpiration(),
                driver.getUser().getId()
        );
    }
}
