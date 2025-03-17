package com.study.projects.percursos_van.repository.projection;

import java.time.LocalDate;

public interface DriverProjection {
    Integer getId();
    String getCnh();
    Character getCnhCat();
    LocalDate getCnhExpiration();
    Integer getUserId();
}
