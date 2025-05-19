package com.isaias.finance.category_service.domain.service;

import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
import com.isaias.finance.category_service.data.dto.UserCategoriesResponseDTO;
import com.isaias.finance.category_service.data.dto.UserCategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String jwtAuth);

    UserCategoriesResponseDTO getAllUserCategories(String jwtAuth);

    UserCategoryResponseDTO getCategoryById(Long id, String jwtAuth);

    List<UserCategoryResponseDTO> searchCategoriesByName(String name, String jwtAuth);
}