package com.isaias.finance.user_service.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}