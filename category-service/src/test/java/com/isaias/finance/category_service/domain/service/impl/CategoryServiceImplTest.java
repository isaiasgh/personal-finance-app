package com.isaias.finance.category_service.domain.service.impl;

import com.isaias.finance.category_service.config.security.JwtProvider;
import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.data.entity.Category;
import com.isaias.finance.category_service.data.mapper.CategoryMapper;
import com.isaias.finance.category_service.data.repository.CategoryRepository;
import com.isaias.finance.category_service.domain.client.UserAuthClient;
import com.isaias.finance.category_service.domain.exception.CategoryAlreadyExistsException;
import com.isaias.finance.category_service.domain.exception.CategoryNotBelongsToUserException;
import com.isaias.finance.category_service.domain.exception.CategoryNotFoundException;
import com.isaias.finance.category_service.domain.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserAuthClient userAuthClient;

    @InjectMocks
    private CategoryServiceImpl subject;

    private CategoryCreationRequestDTO creationRequest;
    private CategoryCreationResponseDTO creationResponse;
    private Category category;

    private String validJwt;
    private String invalidJwt;
    private String username;

    private Category baseCategoryHealth;
    private Category baseCategoryEntertainment;
    private Category baseCategoryTransport;
    private Category baseCategoryFood;

    private Category category2;

    private CategoryResponseDTO baseCategoryDTOHealth;
    private CategoryResponseDTO baseCategoryDTOEntertainment;
    private CategoryResponseDTO baseCategoryDTOTransport;
    private CategoryResponseDTO baseCategoryDTOFood;

    private Category userCategory;
    private UserCategoryResponseDTO userCategoryDTO;
    private UserCategoryResponseDTO userCategoryDTO2;

    private Long categoryId;
    private String searchName;

    @BeforeEach
    void setUp() {
        validJwt = "valid.jwt.token";
        invalidJwt = "invalid.jwt.token";
        username = "john.doe";

        categoryId = 1L;

        searchName = "Book";

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Bookstore");
        category2.setUsername(username);

        userCategoryDTO2 = new UserCategoryResponseDTO();
        userCategoryDTO2.setId(2L);
        userCategoryDTO2.setName("Bookstore");
        userCategoryDTO2.setUsername(username);

        creationRequest = new CategoryCreationRequestDTO();
        creationRequest.setName("Books");

        category = new Category();
        category.setId(1L);
        category.setName("Books");
        category.setUsername(username);

        creationResponse = new CategoryCreationResponseDTO();
        creationResponse.setId(1L);
        creationResponse.setName("Books");

        baseCategoryHealth = new Category();
        baseCategoryHealth.setName("Health");

        baseCategoryEntertainment = new Category();
        baseCategoryEntertainment.setName("Entertainment");

        baseCategoryTransport = new Category();
        baseCategoryTransport.setName("Transport");

        baseCategoryFood = new Category();
        baseCategoryFood.setName("Food");

        baseCategoryDTOHealth = new CategoryResponseDTO();
        baseCategoryDTOHealth.setName("Health");

        baseCategoryDTOEntertainment = new CategoryResponseDTO();
        baseCategoryDTOEntertainment.setName("Entertainment");

        baseCategoryDTOTransport = new CategoryResponseDTO();
        baseCategoryDTOTransport.setName("Transport");

        baseCategoryDTOFood = new CategoryResponseDTO();
        baseCategoryDTOFood.setName("Food");

        userCategory = new Category();
        userCategory.setName("MyCategory");
        userCategory.setUsername(username);

        userCategoryDTO = new UserCategoryResponseDTO();
        userCategoryDTO.setName("Books");
        userCategoryDTO.setUsername(username);
    }

    @Test
    @DisplayName("Should throw exception if category already exists for user")
    void shouldThrowExceptionIfCategoryExistsForUser() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", username)).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> subject.createNewCategory(creationRequest, validJwt));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if base category exists")
    void shouldThrowExceptionIfBaseCategoryExists() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", username)).thenReturn(false);
        when(repository.existsByNameAndUsernameIsNull("Books")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> subject.createNewCategory(creationRequest, validJwt));
    }

    @Test
    @DisplayName("Should create new category successfully")
    void shouldCreateNewCategorySuccessfully() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", username)).thenReturn(false);
        when(repository.existsByNameAndUsernameIsNull("Books")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);
        when(mapper.categoryToCategoryCreationResponseDTO(category)).thenReturn(creationResponse);

        CategoryCreationResponseDTO response = subject.createNewCategory(creationRequest, validJwt);

        assertEquals("Books", response.getName());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException if JWT token is invalid")
    void shouldThrowUnauthorizedExceptionIfJwtIsInvalid() {
        when(jwtProvider.validateToken(invalidJwt)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.createNewCategory(creationRequest, invalidJwt));

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException if user is not valid")
    void shouldThrowUnauthorizedExceptionIfUserIsNotValid() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.createNewCategory(creationRequest, validJwt));
    }

    @Test
    @DisplayName("Should return user categories response DTO when user is valid")
    void shouldReturnUserCategoriesResponseDTOWhenUserIsValid() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        when(repository.findByName("Health")).thenReturn(Optional.of(baseCategoryHealth));
        when(repository.findByName("Entertainment")).thenReturn(Optional.of(baseCategoryEntertainment));
        when(repository.findByName("Transport")).thenReturn(Optional.of(baseCategoryTransport));
        when(repository.findByName("Food")).thenReturn(Optional.of(baseCategoryFood));

        when(mapper.categoryToCategoryResponseDTO(baseCategoryHealth)).thenReturn(baseCategoryDTOHealth);
        when(mapper.categoryToCategoryResponseDTO(baseCategoryEntertainment)).thenReturn(baseCategoryDTOEntertainment);
        when(mapper.categoryToCategoryResponseDTO(baseCategoryTransport)).thenReturn(baseCategoryDTOTransport);
        when(mapper.categoryToCategoryResponseDTO(baseCategoryFood)).thenReturn(baseCategoryDTOFood);

        when(repository.findCategoriesByUsername(username)).thenReturn(List.of(userCategory));
        when(mapper.categoryToUserCategoryResponseDTO(userCategory)).thenReturn(userCategoryDTO);

        UserCategoriesResponseDTO result = subject.getAllUserCategories(validJwt);

        assertNotNull(result);
        assertEquals(4, result.getBaseCategories().size());
        assertEquals(1, result.getUserCategories().size());

        verify(jwtProvider).validateToken(validJwt);
        verify(jwtProvider).getUsername(validJwt);
        verify(userAuthClient).isUsernameValid(username);
        verify(repository).findByName("Health");
        verify(repository).findByName("Entertainment");
        verify(repository).findByName("Transport");
        verify(repository).findByName("Food");
        verify(repository).findCategoriesByUsername(username);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when JWT token is invalid for getAllUserCategories")
    void shouldThrowUnauthorizedExceptionWhenJwtIsInvalidForGetAllUserCategories() {
        when(jwtProvider.validateToken(invalidJwt)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.getAllUserCategories(invalidJwt));

        verify(jwtProvider).validateToken(invalidJwt);
        verify(jwtProvider, never()).getUsername(any());
        verify(userAuthClient, never()).isUsernameValid(any());
        verify(repository, never()).findByName(any());
        verify(repository, never()).findCategoriesByUsername(any());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when user is not valid for getAllUserCategories")
    void shouldThrowUnauthorizedExceptionWhenUserIsNotValidForGetAllUserCategories() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.getAllUserCategories(validJwt));

        verify(jwtProvider).validateToken(validJwt);
        verify(jwtProvider).getUsername(validJwt);
        verify(userAuthClient).isUsernameValid(username);
        verify(repository, never()).findByName(any());
        verify(repository, never()).findCategoriesByUsername(any());
    }

    @Test
    @DisplayName("Should return UserCategoryResponseDTO if category exists and belongs to user")
    void shouldReturnUserCategoryResponseDTOIfCategoryExistsAndBelongsToUser() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        when(repository.findById(categoryId)).thenReturn(Optional.of(category));
        when(mapper.categoryToUserCategoryResponseDTO(category)).thenReturn(userCategoryDTO);

        UserCategoryResponseDTO result = subject.getCategoryById(categoryId, validJwt);

        assertNotNull(result);
        assertEquals("Books", result.getName());
        assertEquals(username, result.getUsername());

        verify(repository).findById(categoryId);
        verify(mapper).categoryToUserCategoryResponseDTO(category);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException if category does not exist")
    void shouldThrowCategoryNotFoundExceptionIfCategoryDoesNotExist() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> subject.getCategoryById(categoryId, validJwt));

        verify(repository).findById(categoryId);
        verify(mapper, never()).categoryToUserCategoryResponseDTO(any());
    }

    @Test
    @DisplayName("Should throw CategoryNotBelongsToUserException if category does not belong to user")
    void shouldThrowCategoryNotBelongsToUserExceptionIfCategoryNotBelongsToUser() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        Category otherUserCategory = new Category();
        otherUserCategory.setId(categoryId);
        otherUserCategory.setName("Books");
        otherUserCategory.setUsername("other.user");

        when(repository.findById(categoryId)).thenReturn(Optional.of(otherUserCategory));

        assertThrows(CategoryNotBelongsToUserException.class,
                () -> subject.getCategoryById(categoryId, validJwt));

        verify(repository).findById(categoryId);
        verify(mapper, never()).categoryToUserCategoryResponseDTO(any());
    }

    @Test
    @DisplayName("Should return list of UserCategoryResponseDTO matching name and username")
    void shouldReturnListOfUserCategoryResponseDTOMatchingNameAndUsername() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        List<Category> categories = List.of(category, category2);
        when(repository.findCategoriesByNameContainingAndUsername(searchName, username)).thenReturn(categories);

        when(mapper.categoryToUserCategoryResponseDTO(category)).thenReturn(userCategoryDTO);
        when(mapper.categoryToUserCategoryResponseDTO(category2)).thenReturn(userCategoryDTO2);

        List<UserCategoryResponseDTO> results = subject.searchCategoriesByName(searchName, validJwt);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Books", results.get(0).getName());
        assertEquals("Bookstore", results.get(1).getName());

        verify(repository).findCategoriesByNameContainingAndUsername(searchName, username);
        verify(mapper).categoryToUserCategoryResponseDTO(category);
        verify(mapper).categoryToUserCategoryResponseDTO(category2);
    }

    @Test
    @DisplayName("Should return empty list if no categories found")
    void shouldReturnEmptyListIfNoCategoriesFound() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);

        when(repository.findCategoriesByNameContainingAndUsername(searchName, username)).thenReturn(Collections.emptyList());

        List<UserCategoryResponseDTO> results = subject.searchCategoriesByName(searchName, validJwt);

        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(repository).findCategoriesByNameContainingAndUsername(searchName, username);
        verify(mapper, never()).categoryToUserCategoryResponseDTO(any());
    }

    @Test
    @DisplayName("searchCategoriesByName: should throw UnauthorizedException if JWT token is invalid")
    void searchCategoriesByName_shouldThrowUnauthorizedExceptionIfJwtIsInvalid() {
        when(jwtProvider.validateToken(invalidJwt)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.searchCategoriesByName(searchName, invalidJwt));

        verify(repository, never()).findCategoriesByNameContainingAndUsername(any(), any());
        verify(mapper, never()).categoryToUserCategoryResponseDTO(any());
    }

    @Test
    @DisplayName("searchCategoriesByName: should throw UnauthorizedException if user is not valid")
    void searchCategoriesByName_shouldThrowUnauthorizedExceptionIfUserIsNotValid() {
        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.searchCategoriesByName(searchName, validJwt));

        verify(repository, never()).findCategoriesByNameContainingAndUsername(any(), any());
        verify(mapper, never()).categoryToUserCategoryResponseDTO(any());
    }

    @Test
    @DisplayName("updateCategory: should update category successfully")
    void updateCategory_shouldUpdateCategorySuccessfully() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setName("UpdatedBooks");

        category.setUsername(username);

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("UpdatedBooks");
        updatedCategory.setUsername(username);

        UserCategoryResponseDTO updatedResponse = new UserCategoryResponseDTO();
        updatedResponse.setId(categoryId);
        updatedResponse.setName("UpdatedBooks");
        updatedResponse.setUsername(username);

        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.findById(categoryId)).thenReturn(Optional.of(category));
        when(repository.save(any(Category.class))).thenReturn(updatedCategory);
        when(mapper.categoryToUserCategoryResponseDTO(updatedCategory)).thenReturn(updatedResponse);

        UserCategoryResponseDTO result = subject.updateCategory(categoryId, updateDTO, validJwt);

        assertEquals("UpdatedBooks", result.getName());
        assertEquals(username, result.getUsername());
        verify(repository).save(any(Category.class));
    }

    @Test
    @DisplayName("updateCategory: should throw UnauthorizedException if JWT token is invalid")
    void updateCategory_shouldThrowUnauthorizedExceptionIfJwtIsInvalid() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setName("UpdatedBooks");

        when(jwtProvider.validateToken(invalidJwt)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.updateCategory(categoryId, updateDTO, invalidJwt));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("updateCategory: should throw UnauthorizedException if user is not valid")
    void updateCategory_shouldThrowUnauthorizedExceptionIfUserIsNotValid() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setName("UpdatedBooks");

        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.updateCategory(categoryId, updateDTO, validJwt));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("updateCategory: should throw CategoryNotFoundException if category does not exist")
    void updateCategory_shouldThrowCategoryNotFoundExceptionIfCategoryNotFound() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setName("UpdatedBooks");

        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> subject.updateCategory(categoryId, updateDTO, validJwt));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("updateCategory: should throw CategoryNotBelongsToUserException if category belongs to another user")
    void updateCategory_shouldThrowCategoryNotBelongsToUserExceptionIfNotOwner() {
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setName("UpdatedBooks");

        category.setUsername("another.user");

        when(jwtProvider.validateToken(validJwt)).thenReturn(true);
        when(jwtProvider.getUsername(validJwt)).thenReturn(username);
        when(userAuthClient.isUsernameValid(username)).thenReturn(true);
        when(repository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThrows(CategoryNotBelongsToUserException.class,
                () -> subject.updateCategory(categoryId, updateDTO, validJwt));

        verify(repository, never()).save(any());
    }

}