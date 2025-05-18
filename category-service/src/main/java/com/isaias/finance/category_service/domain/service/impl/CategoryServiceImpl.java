package com.isaias.finance.category_service.domain.service.impl;

import com.isaias.finance.category_service.config.security.JwtProvider;
import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
import com.isaias.finance.category_service.data.entity.Category;
import com.isaias.finance.category_service.data.mapper.CategoryMapper;
import com.isaias.finance.category_service.data.repository.CategoryRepository;
import com.isaias.finance.category_service.domain.exception.CategoryAlreadyExistsException;
import com.isaias.finance.category_service.domain.service.CategoryService;
import com.isaias.finance.category_service.domain.client.UserAuthClient;
import com.isaias.finance.category_service.domain.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final JwtProvider jwtProvider;
    private final UserAuthClient userAuthClient;

    @Override
    public CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        boolean exists = repository
                .existsByNameAndUsername (
                        category.getName(),
                        username
                );

        if (exists) throw new CategoryAlreadyExistsException("Category with name: " + category.getName() + " for user: " + username + " already exists.");

        Category newCategory = new Category ();
        newCategory.setName(category.getName());
        newCategory.setUsername(username);

        return mapper.categoryToCategoryCreationResponseDTO (repository.save(newCategory));
    }

    private String getValidateUsername (String jwtToken) {
        if (!jwtProvider.validateToken(jwtToken)) {
            throw new UnauthorizedException("Token not valid");
        }

        return jwtProvider.getUsername(jwtToken);
    }

    private void verifyUsernameWithUserService (String username) {
        boolean exists = userAuthClient.isUsernameValid(username);

        if (!exists) {
            throw new UnauthorizedException("User not valid");
        }
    }
}
