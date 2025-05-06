package com.isaias.finance.user_service.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegistrationResponseDTO {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}