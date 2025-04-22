package com.study.projects.percursos_van.model;

import com.study.projects.percursos_van.model.enums.Role;
import com.study.projects.percursos_van.web.controller.dto.user.UserCreateDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Column(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "last_modified_at")
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Phone> phones = new ArrayList<>();

    public void addPhone(Phone phone) {
        this.phones.addFirst(phone);
    }

    public void addManyPhones(List<Phone> phones) {
        this.phones.addAll(phones);
    }

    public User(UserCreateDTO dto) {
        this.fullName = dto.fullName();
        this.email = dto.email();
        this.password = dto.password();
        this.cpf = dto.cpf();
    }
}
