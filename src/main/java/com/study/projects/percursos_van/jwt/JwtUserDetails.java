package com.study.projects.percursos_van.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    private final com.study.projects.percursos_van.model.User user;
    public JwtUserDetails(com.study.projects.percursos_van.model.User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public Integer getId(){
        return user.getId();
    }

    public String getRole(){
        return user.getRole().name();
    }
}
