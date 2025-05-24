package com.isaias.finance.category_service.domain.service.impl;

import com.isaias.finance.category_service.config.security.JwtProvider;
import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.data.entity.Category;
import com.isaias.finance.category_service.data.mapper.CategoryMapper;
import com.isaias.finance.category_service.data.repository.CategoryRepository;
import com.isaias.finance.category_service.domain.client.UserAuthClient;
import com.isaias.finance.category_service.domain.exception.CategoryAlreadyExistsException;
import com.isaias.finance.category_service.domain.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private CategoryResponseDTO baseCategoryDTOHealth;
    private CategoryResponseDTO baseCategoryDTOEntertainment;
    private CategoryResponseDTO baseCategoryDTOTransport;
    private CategoryResponseDTO baseCategoryDTOFood;

    private Category userCategory;
    private UserCategoryResponseDTO userCategoryDTO;

    @BeforeEach
    void setUp() {
        validJwt = "valid.jwt.token";
        invalidJwt = "invalid.jwt.token";
        username = "john.doe";

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
        userCategoryDTO.setName("MyCategory");
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

    // Tests para getAllUserCategories

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
}