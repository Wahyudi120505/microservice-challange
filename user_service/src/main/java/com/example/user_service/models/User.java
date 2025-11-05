package com.example.user_service.models;

import org.hibernate.annotations.UuidGenerator;

import com.example.user_service.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable=false)
    private String id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", length = 2000, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
