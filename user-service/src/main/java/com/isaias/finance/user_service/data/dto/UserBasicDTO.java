package com.isaias.finance.user_service.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBasicDTO {
    private Integer id;
    private String name;
    private String lastName;
    private String username;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
}