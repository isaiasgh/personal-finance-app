package com.isaias.finance.user_service.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PasswordLog {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private String hashedPassword;

    @Column (nullable = false)
    private LocalDateTime attemptTimestamp;

    private String loginError;

    private Boolean isCurrent;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;
}