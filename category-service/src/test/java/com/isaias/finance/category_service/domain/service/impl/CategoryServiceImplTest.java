package com.isaias.finance.category_service.domain.service.impl;

import com.isaias.finance.category_service.config.security.JwtProvider;
import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void setUp() {
        creationRequest = new CategoryCreationRequestDTO();
        creationRequest.setName("Books");

        category = new Category();
        category.setId(1L);
        category.setName("Books");
        category.setUsername("john.doe");

        creationResponse = new CategoryCreationResponseDTO();
        creationResponse.setId(1L);
        creationResponse.setName("Books");
    }

    @Test
    @DisplayName("Should throw exception if category already exists for user")
    void shouldThrowExceptionIfCategoryExistsForUser() {
        when(jwtProvider.validateToken("jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("jwt.token")).thenReturn("john.doe");
        when(userAuthClient.isUsernameValid("john.doe")).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", "john.doe")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> subject.createNewCategory(creationRequest, "jwt.token"));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if base category exists")
    void shouldThrowExceptionIfBaseCategoryExists() {
        when(jwtProvider.validateToken("jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("jwt.token")).thenReturn("john.doe");
        when(userAuthClient.isUsernameValid("john.doe")).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", "john.doe")).thenReturn(false);
        when(repository.existsByNameAndUsernameIsNull("Books")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> subject.createNewCategory(creationRequest, "jwt.token"));
    }

    @Test
    @DisplayName("Should create new category successfully")
    void shouldCreateNewCategorySuccessfully() {
        when(jwtProvider.validateToken("jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("jwt.token")).thenReturn("john.doe");
        when(userAuthClient.isUsernameValid("john.doe")).thenReturn(true);
        when(repository.existsByNameAndUsername("Books", "john.doe")).thenReturn(false);
        when(repository.existsByNameAndUsernameIsNull("Books")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);
        when(mapper.categoryToCategoryCreationResponseDTO(category)).thenReturn(creationResponse);

        CategoryCreationResponseDTO response = subject.createNewCategory(creationRequest, "jwt.token");

        assertEquals("Books", response.getName());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException if JWT token is invalid")
    void shouldThrowUnauthorizedExceptionIfJwtIsInvalid() {
        when(jwtProvider.validateToken("invalid.token")).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.createNewCategory(creationRequest, "invalid.token"));

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException if user is not valid")
    void shouldThrowUnauthorizedExceptionIfUserIsNotValid() {
        when(jwtProvider.validateToken("jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("jwt.token")).thenReturn("john.doe");
        when(userAuthClient.isUsernameValid("john.doe")).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> subject.createNewCategory(creationRequest, "jwt.token"));
    }
}
