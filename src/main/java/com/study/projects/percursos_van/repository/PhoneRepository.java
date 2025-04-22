package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {

    Optional<Phone> findByNumber(String number);
}
