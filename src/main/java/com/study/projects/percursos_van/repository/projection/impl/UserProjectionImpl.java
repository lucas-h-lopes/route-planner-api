package com.study.projects.percursos_van.repository.projection.impl;

import com.study.projects.percursos_van.model.User;
import com.study.projects.percursos_van.repository.projection.UserProjection;
import com.study.projects.percursos_van.web.mapper.phone.PhoneMapper;

public class UserProjectionImpl implements UserProjection {
    private final Integer id;
    private final String fullName;
    private final String cpf;
    private final String email;
    private final String role;
    private final String[] phones;

    public UserProjectionImpl(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.cpf = user.getCpf();
        this.phones = PhoneMapper.getNumbersFromPhonesArr(user.getPhones());
    }

    @Override
    public String[] getPhones() {
        return this.phones;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getFullName() {
        return this.fullName;
    }

    @Override
    public String getCpf() {
        return this.cpf;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getRole() {
        return this.role;
    }
}
