package com.isaias.finance.category_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCategoryResponseDTO {
    private Long id;
    private String name;
    private String username;
}