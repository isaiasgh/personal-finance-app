package com.isaias.finance.category_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCategoriesResponseDTO {
    private List <CategoryResponseDTO> baseCategories;
    private List <UserCategoryResponseDTO> userCategories;
}