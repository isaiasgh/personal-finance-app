package com.isaias.finance.category_service.domain.service;

import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;

public interface CategoryService {
    CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String username);
}