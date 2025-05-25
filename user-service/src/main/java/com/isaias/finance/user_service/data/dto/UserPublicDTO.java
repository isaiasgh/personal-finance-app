package com.isaias.finance.user_service.data.dto;

import lombok.Data;

@Data
public class UserPublicDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String username;
}