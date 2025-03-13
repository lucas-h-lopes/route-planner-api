package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    Driver findByUser(User user);
}
