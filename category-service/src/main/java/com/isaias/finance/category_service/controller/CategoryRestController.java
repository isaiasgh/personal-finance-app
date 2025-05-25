package com.isaias.finance.category_service.controller;

import com.isaias.finance.category_service.config.OpenApiConfig;
import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.domain.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
public class CategoryRestController {
    private final CategoryService categoryService;

    @Operation(summary = "Create a new category for the authenticated user")
    @PostMapping
    public ResponseEntity <CategoryCreationResponseDTO> createNewCategory (@RequestBody @Valid CategoryCreationRequestDTO category, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        return new ResponseEntity <> (
                categoryService.createNewCategory (category, jwt),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT missing or invalid")
            })
    @GetMapping()
    public UserCategoriesResponseDTO getAllUserCategories (@RequestHeader("Authorization") String jwt) {
        return categoryService.getAllUserCategories(jwt);
    }

    @Operation(summary = "Get a specific category by ID for the authenticated user")
    @GetMapping("/{id}")
    public UserCategoryResponseDTO getCategoryById (@RequestHeader("Authorization") String jwtAuth, @PathVariable Long id) {
        return categoryService.getCategoryById (id, jwtAuth);
    }

    @Operation(summary = "Search categories by name for the authenticated user",
        responses = {
                @ApiResponse(responseCode = "200", description = "Search completed"),
                @ApiResponse(responseCode = "401", description = "Unauthorized - JWT missing or invalid")
        })
    @GetMapping("/search")
    public List<UserCategoryResponseDTO> searchCategoriesByName (
            @RequestParam String name,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        return categoryService.searchCategoriesByName(name, jwtAuth);
    }

    @Operation(summary = "Update a category for the authenticated user")
    @PutMapping("/{id}")
    public UserCategoryResponseDTO updateCategory (
            @RequestBody @Valid CategoryUpdateDTO categoryUpdateDTO,
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        return categoryService.updateCategory(id, categoryUpdateDTO, jwtAuth);
    }

    @Operation(summary = "Delete a category for the authenticated user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory (
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        categoryService.deleteCategory(id, jwtAuth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}