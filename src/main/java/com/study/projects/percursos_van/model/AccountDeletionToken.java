package com.study.projects.percursos_van.model;

import com.study.projects.percursos_van.model.interfaces.AccountToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AccountDeletionToken implements AccountToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public LocalDateTime getExpirationDate() {
        return this.expiresAt;
    }
}
