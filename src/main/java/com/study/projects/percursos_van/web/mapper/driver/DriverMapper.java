package com.study.projects.percursos_van.web.mapper.driver;

import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverMapper {

    public static Driver toDriver(DriverCreateDTO dto) {
        return dto.toDriver();
    }

    public static DriverResponseDTO toResponseDTO(Driver driver) {
        return DriverResponseDTO.toDriverResponseDTO(driver);
    }
}
