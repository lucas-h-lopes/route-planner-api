package com.study.projects.percursos_van.service;

import com.study.projects.percursos_van.exception.DuplicatedCnhException;
import com.study.projects.percursos_van.exception.NotFoundException;
import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    @Transactional
    public Driver insert(Driver driver) {
        try {
            return driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedCnhException("A CNH '" + driver.getCnh() + "' já está cadastrada no sistema");
        }
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
