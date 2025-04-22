package com.study.projects.percursos_van.repository.projection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "fullName", "cpf", "email", "role"})
public interface UserProjection {

    Integer getId();
    String getFullName();
    String getCpf();
    String getEmail();
    String getRole();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String[] getPhones();
}
