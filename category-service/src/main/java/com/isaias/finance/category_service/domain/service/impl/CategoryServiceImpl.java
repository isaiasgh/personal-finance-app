package com.isaias.finance.category_service.domain.service.impl;

import com.isaias.finance.category_service.config.security.JwtProvider;
import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.data.entity.Category;
import com.isaias.finance.category_service.data.mapper.CategoryMapper;
import com.isaias.finance.category_service.data.repository.CategoryRepository;
import com.isaias.finance.category_service.domain.exception.CategoryAlreadyExistsException;
import com.isaias.finance.category_service.domain.exception.CategoryNotBelongsToUserException;
import com.isaias.finance.category_service.domain.exception.CategoryNotFoundException;
import com.isaias.finance.category_service.domain.service.CategoryService;
import com.isaias.finance.category_service.domain.client.UserAuthClient;
import com.isaias.finance.category_service.domain.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final JwtProvider jwtProvider;
    private final UserAuthClient userAuthClient;

    private List <String> baseCategoriesNames = List.of ("Health", "Entertainment", "Transport", "Food");

    @Override
    public CategoryCreationResponseDTO createNewCategory(CategoryCreationRequestDTO category, String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        boolean exists = repository
                .existsByNameAndUsername (
                        category.getName(),
                        username
                );

        boolean existsBaseCategory = repository
                .existsByNameAndUsernameIsNull(category.getName());

        if (exists) throw new CategoryAlreadyExistsException("Category with name: " + category.getName() + " for user: " + username + " already exists.");
        if (existsBaseCategory) throw new CategoryAlreadyExistsException("Category with name: " + category.getName() + " is a base category.");

        Category newCategory = new Category ();
        newCategory.setName(category.getName());
        newCategory.setUsername(username);

        return mapper.categoryToCategoryCreationResponseDTO (repository.save(newCategory));
    }

    @Override
    public UserCategoriesResponseDTO getAllUserCategories(String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        return buildUserCategoriesResponse (username);
    }

    @Override
    public UserCategoryResponseDTO getCategoryById(Long id, String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " was not found."));

        if (category.getUsername().equals(username)) {
            return mapper.categoryToUserCategoryResponseDTO(category);
        }

        throw new CategoryNotBelongsToUserException("Category with id: " + id + " does not belong to user: " + username);
    }

    @Override
    public List<UserCategoryResponseDTO> searchCategoriesByName(String name, String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        return repository.findCategoriesByNameContainingAndUsername(name, username).stream()
                .map(mapper::categoryToUserCategoryResponseDTO)
                .toList();
    }

    @Override
    public UserCategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO, String jwtAuth) {
        String username = getValidateUsername(jwtAuth);
        verifyUsernameWithUserService(username);

        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " was not found"));

        boolean categoryBelongsToUser = category.getUsername().equals(username);

        if (!categoryBelongsToUser) {
            throw new CategoryNotBelongsToUserException("Category with id: " + id + " does not belong to user: " + username);
        }

        category.setName(categoryUpdateDTO.getName());
        return mapper.categoryToUserCategoryResponseDTO(repository.save(category));
    }

    private UserCategoriesResponseDTO buildUserCategoriesResponse(String username) {
        List<CategoryResponseDTO> baseCategories = baseCategoriesNames.stream()
                .map(categoryName -> repository.findByName(categoryName)
                        .map(mapper::categoryToCategoryResponseDTO)
                        .orElseThrow(() -> new CategoryNotFoundException("Category " + categoryName + " was not found")))
                .toList();

        List<UserCategoryResponseDTO> userCategories = repository
                .findCategoriesByUsername(username).stream()
                .map(mapper::categoryToUserCategoryResponseDTO)
                .toList();

        return new UserCategoriesResponseDTO(baseCategories, userCategories);
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
