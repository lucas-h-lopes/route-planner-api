package com.study.projects.percursos_van.web.mapper.driver;

import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.model.Phone;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverResponseDTO;
import com.study.projects.percursos_van.web.mapper.phone.PhoneMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverMapper {

    public static Driver toDriver(DriverCreateDTO dto) {
        User user = new User();
        user.setCpf(dto.cpf());
        user.setRole(Role.DRIVER);
        user.setEmail(dto.email());
        user.setFullName(dto.fullName());
        user.setPassword(dto.password());
        user.addPhone(PhoneMapper.toPrincipalPhone(dto.phone(), user));
        if (dto.otherPhones() != null) {
            user.addManyPhones(Arrays.stream(dto.otherPhones())
                    .map(x -> new Phone(x, Phone.PhoneType.SECONDARY, user)).toList());
        }

        Driver driver = dto.toDriver();
        driver.setUser(user);
        return driver;
    }

    public static DriverResponseDTO toResponseDTO(Driver driver) {
        return DriverResponseDTO.toDriverResponseDTO(driver);
    }
}
