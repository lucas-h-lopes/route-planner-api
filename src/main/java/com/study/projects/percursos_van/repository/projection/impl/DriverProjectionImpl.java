package com.study.projects.percursos_van.repository.projection.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.repository.projection.DriverProjection;

import java.time.LocalDate;

public class DriverProjectionImpl implements DriverProjection {

    private Integer id;
    private String cnh;
    private Character cnhCat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate cnhExpiration;
    private Integer userId;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getCnh() {
        return this.cnh;
    }

    @Override
    public Character getCnhCat() {
        return this.cnhCat;
    }

    @Override
    public LocalDate getCnhExpiration() {
        return this.cnhExpiration;
    }

    @Override
    public Integer getUserId() {
        return this.userId;
    }

    public DriverProjectionImpl(Driver driver){
        this.id = driver.getId();
        this.cnh = driver.getCnh();
        this.cnhCat = driver.getCnhCat();
        this.cnhExpiration = driver.getCnhExpiration();
        this.userId = driver.getUser().getId();
    }
}
