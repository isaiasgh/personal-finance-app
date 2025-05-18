package com.isaias.finance.category_service.data.mapper;

import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
import com.isaias.finance.category_service.data.entity.Category;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface CategoryMapper {
    CategoryCreationResponseDTO categoryToCategoryCreationResponseDTO (Category category);
}