package com.isaias.finance.user_service.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "user_basic_data")
@Data
public class User {
    @Id
    @Column (name = "user_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "user_name", nullable = false)
    private String name;

    @Column (name = "user_lastName", nullable = false)
    private String lastName;

    @Column (name = "user_email", nullable = false, unique = true)
    private String email;

    @Column (nullable = false, unique = true)
    private String username;
}