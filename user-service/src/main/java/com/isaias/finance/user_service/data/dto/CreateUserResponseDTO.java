package com.isaias.finance.user_service.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateUserResponseDTO {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}