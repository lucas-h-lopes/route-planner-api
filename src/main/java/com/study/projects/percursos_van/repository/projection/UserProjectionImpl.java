package com.study.projects.percursos_van.repository.projection;

import com.study.projects.percursos_van.model.User;

public class UserProjectionImpl implements UserProjection{
    private final Integer id;
    private final String fullName;
    private final String cpf;
    private final String email;
    private final String role;

    public UserProjectionImpl(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.cpf = user.getCpf();
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
