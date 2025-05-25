package com.isaias.finance.category_service.domain.service;

import com.isaias.finance.category_service.data.dto.*;

import java.util.List;

public interface CategoryService {
    CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String jwtAuth);

    UserCategoriesResponseDTO getAllUserCategories(String jwtAuth);

    UserCategoryResponseDTO getCategoryById(Long id, String jwtAuth);

    List<UserCategoryResponseDTO> searchCategoriesByName(String name, String jwtAuth);

    UserCategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO, String jwtAuth);

    void deleteCategory(Long id, String jwtAuth);
}