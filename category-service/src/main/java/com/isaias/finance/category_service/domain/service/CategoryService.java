package com.isaias.finance.category_service.domain.service;

import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
import com.isaias.finance.category_service.data.dto.UserCategoriesResponseDTO;

public interface CategoryService {
    CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String jwtAuth);

    UserCategoriesResponseDTO getAllUserCategories(String jwtAuth);
}