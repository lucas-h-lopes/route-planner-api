package com.study.projects.percursos_van.repository;

import com.study.projects.percursos_van.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
}
