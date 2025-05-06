package com.isaias.finance.user_service.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;
}