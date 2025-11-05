package com.example.user_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "user_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable=false)
    private String id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean expired;
    private boolean revoked;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
