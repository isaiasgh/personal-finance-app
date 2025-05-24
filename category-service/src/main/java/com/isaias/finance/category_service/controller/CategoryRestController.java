package com.isaias.finance.category_service.controller;

import com.isaias.finance.category_service.data.dto.*;
import com.isaias.finance.category_service.domain.service.CategoryService;
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
public class CategoryRestController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity <CategoryCreationResponseDTO> createNewCategory (@RequestBody @Valid CategoryCreationRequestDTO category, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        return new ResponseEntity <> (
                categoryService.createNewCategory (category, jwt),
                HttpStatus.CREATED);
    }

    @GetMapping()
    public UserCategoriesResponseDTO getAllUserCategories (@RequestHeader("Authorization") String jwt) {
        return categoryService.getAllUserCategories(jwt);
    }

    @GetMapping("/{id}")
    public UserCategoryResponseDTO getCategoryById (@RequestHeader("Authorization") String jwtAuth, @PathVariable Long id) {
        return categoryService.getCategoryById (id, jwtAuth);
    }

    @GetMapping("/search")
    public List<UserCategoryResponseDTO> searchCategoriesByName (
            @RequestParam String name,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        return categoryService.searchCategoriesByName(name, jwtAuth);
    }

    @PutMapping("/{id}")
    public UserCategoryResponseDTO updateCategory (
            @RequestBody @Valid CategoryUpdateDTO categoryUpdateDTO,
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        return categoryService.updateCategory(id, categoryUpdateDTO, jwtAuth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory (
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwtAuth
    ) {
        categoryService.deleteCategory(id, jwtAuth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}