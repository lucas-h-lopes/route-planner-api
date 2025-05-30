package com.study.projects.percursos_van.model;

import com.study.projects.percursos_van.model.interfaces.AccountToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AccountEmailChangeToken implements AccountToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "new_email", nullable = false)
    private String newEmail;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Override
    public LocalDateTime getExpirationDate() {
        return this.expiresAt;
    }
}
