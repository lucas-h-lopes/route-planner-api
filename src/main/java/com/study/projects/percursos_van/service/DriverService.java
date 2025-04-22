package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedCnhException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.DriverRepository;
import com.study.projects.percursos_van.repository.projection.DriverProjection;
import com.study.projects.percursos_van.repository.projection.impl.DriverProjectionImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserService userService;
    private final PhoneService phoneService;

    @Transactional
    public Driver insert(Driver driver) {
        try {
            userService.insert(driver.getUser());
            User user = driver.getUser();

            if (driver.getCnhCat() != null) {
                driver.setCnhCat(Character.toUpperCase(driver.getCnhCat()));
            }
            phoneService.insertMany(user.getPhones().getFirst(), phoneService.getOtherPhones(user));
            return driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedCnhException("Algo de errado durante a inserção do motorista");
        }
    }

    @Transactional(readOnly = true)
    public Driver findById(Integer id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recurso solicitado não foi encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<DriverProjection> findAll(String cnhCat, Pageable pageable) {
        Driver driver = new Driver();
        if (cnhCat != null && cnhCat.length() == 1) {
            driver.setCnhCat(Character.toUpperCase(cnhCat.charAt(0)));
        }

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues();

        Example<Driver> example = Example.of(driver, exampleMatcher);


        Page<Driver> driverPage = driverRepository.findAll(example, pageable);

        return driverPage.map(DriverProjectionImpl::new);
    }

    @Transactional
    public Driver getByUser(User user) {
        Driver driver = driverRepository.findByUser(user);
        if (driver == null) {
            throw new NotFoundException("Perfil de motorista não foi encontrado para este usuário.");
        }
        return driver;
    }

    @Transactional
    public void deleteByUser(User user) {
        Driver driver = getByUser(user);
        driverRepository.delete(driver);
    }


}
