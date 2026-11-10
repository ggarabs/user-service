package com.github.ggarabs.user_service.adapters.outbound;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_tb")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String phone;
    private String email;
    // Armazena somente a senha codificada pelo PasswordEncoder.
    private String password;
    private String birthday;
    private Double latitude;
    private Double longitude;
    private Double preferredRatio;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CANDIDATE;

    @PrePersist
    private void setDefaultRole() {
        if (role == null) role = UserRole.CANDIDATE;
    }
}
