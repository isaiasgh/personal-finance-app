package com.isaias.finance.category_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaias.finance.category_service.config.security.JwtAuthInterceptor;
import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.domain.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CategoryRestController.class)
class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtAuthInterceptor jwtAuthInterceptor;

    @Test
    void createNewCategory_shouldReturnCreatedCategory() throws Exception {
        CategoryCreationRequestDTO request = new CategoryCreationRequestDTO();
        request.setName("Books");

        CategoryCreationResponseDTO response = new CategoryCreationResponseDTO();
        response.setId(1L);
        response.setName("Books");

        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        when(categoryService.createNewCategory(request, "Bearer fake-jwt"))
                .thenReturn(response);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer fake-jwt")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void getAllUserCategories_shouldReturnListOfCategories() throws Exception {
        UserCategoriesResponseDTO response = new UserCategoriesResponseDTO();
        response.setBaseCategories(List.of(
                new CategoryResponseDTO(10L, "General")
        ));

        UserCategoryResponseDTO userCategoryResponseDTO1 = new UserCategoryResponseDTO();
        userCategoryResponseDTO1.setId(1L);
        userCategoryResponseDTO1.setName("Books");

        UserCategoryResponseDTO userCategoryResponseDTO2 = new UserCategoryResponseDTO();
        userCategoryResponseDTO2.setId(2L);
        userCategoryResponseDTO2.setName("Work");

        response.setUserCategories(List.of(
                userCategoryResponseDTO1,
                userCategoryResponseDTO2
        ));

        when(categoryService.getAllUserCategories("Bearer fake-jwt")).thenReturn(response);

        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer fake-jwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseCategories.size()").value(1))
                .andExpect(jsonPath("$.baseCategories[0].id").value(10L))
                .andExpect(jsonPath("$.baseCategories[0].name").value("General"))
                .andExpect(jsonPath("$.userCategories.size()").value(2))
                .andExpect(jsonPath("$.userCategories[0].id").value(1L))
                .andExpect(jsonPath("$.userCategories[0].name").value("Books"))
                .andExpect(jsonPath("$.userCategories[1].id").value(2L))
                .andExpect(jsonPath("$.userCategories[1].name").value("Work"));
    }


    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        UserCategoryResponseDTO response = new UserCategoryResponseDTO();
        response.setId(1L);
        response.setName("Books");

        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        when(categoryService.getCategoryById(1L, "Bearer fake-jwt")).thenReturn(response);

        mockMvc.perform(get("/categories/1")
                        .header("Authorization", "Bearer fake-jwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void searchCategoriesByName_shouldReturnMatchingCategories() throws Exception {
        UserCategoryResponseDTO userCategoryResponseDTO1 = new UserCategoryResponseDTO();
        userCategoryResponseDTO1.setId(1L);
        userCategoryResponseDTO1.setName("Books");

        UserCategoryResponseDTO userCategoryResponseDTO2 = new UserCategoryResponseDTO();
        userCategoryResponseDTO2.setId(2L);
        userCategoryResponseDTO2.setName("Book Club");

        List<UserCategoryResponseDTO> result = List.of(
                userCategoryResponseDTO1,
                userCategoryResponseDTO2
        );

        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        when(categoryService.searchCategoriesByName("Book", "Bearer fake-jwt")).thenReturn(result);

        mockMvc.perform(get("/categories/search")
                        .param("name", "Book")
                        .header("Authorization", "Bearer fake-jwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Books"))
                .andExpect(jsonPath("$[1].name").value("Book Club"));
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        CategoryUpdateDTO request = new CategoryUpdateDTO();
        request.setName("Updated Books");

        UserCategoryResponseDTO response = new UserCategoryResponseDTO();
        response.setId(1L);
        response.setName("Updated Books");

        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        when(categoryService.updateCategory(eq(1L), eq(request), anyString())).thenReturn(response);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer fake-jwt")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Books"));
    }

    @Test
    void deleteCategory_shouldReturnNoContent() throws Exception {
        when(jwtAuthInterceptor.preHandle(any(), any(), any()))
                .thenReturn(true);

        mockMvc.perform(delete("/categories/1")
                        .header("Authorization", "Bearer fake-jwt"))
                .andExpect(status().isNoContent());
    }
}