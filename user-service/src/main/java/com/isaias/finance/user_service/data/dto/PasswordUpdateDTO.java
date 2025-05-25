package com.isaias.finance.user_service.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordUpdateDTO {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}