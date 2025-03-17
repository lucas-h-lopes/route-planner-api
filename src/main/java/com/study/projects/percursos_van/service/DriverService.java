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

    @Transactional
    public Driver insert(Driver driver) {
        try {
            if(driver.getCnhCat() != null){
                driver.setCnhCat(Character.toUpperCase(driver.getCnhCat()));
            }
            return driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedCnhException("A CNH '" + driver.getCnh() + "' já está cadastrada no sistema");
        }
    }

    @Transactional
    public Driver completeProfile(Driver driver, Integer authenticatedId){
        Driver authenticatedDriver = getByUser(userService.findById(authenticatedId));
        authenticatedDriver.setCnh(driver.getCnh());
        authenticatedDriver.setCnhCat(driver.getCnhCat());
        authenticatedDriver.setCnhExpiration(driver.getCnhExpiration());

        return insert(authenticatedDriver);
    }

    @Transactional(readOnly = true)
    public Driver findById(Integer id){
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não existe motorista cadastrado com o id '" + id + "'"));
    }

    @Transactional(readOnly = true)
    public Page<DriverProjection> findAll(String cnhCat, Pageable pageable){
            Driver driver = new Driver();
            if (cnhCat != null && cnhCat.length() == 1) {
                driver.setCnhCat(Character.toUpperCase(cnhCat.charAt(0)));
            }

            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                    .withIgnoreNullValues();

            Example<Driver> example = Example.of(driver, exampleMatcher);


            Page<Driver> driverPage = driverRepository.findAll(/*example,*/ pageable);

            return driverPage.map(DriverProjectionImpl::new);
    }

    @Transactional
    public Driver getByUser(User user){
        try {
            return driverRepository.findByUser(user);
        }catch(Exception e){
            throw new NotFoundException("O usuário não possui perfil de motorista associado");
        }
    }

    @Transactional
    public void deleteByUser(User user){
            Driver driver = getByUser(user);
            driverRepository.delete(driver);
    }
}
