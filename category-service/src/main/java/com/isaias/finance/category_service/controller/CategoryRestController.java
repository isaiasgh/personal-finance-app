package com.isaias.finance.category_service.controller;

import com.isaias.finance.category_service.data.dto.CategoryCreationRequestDTO;
import com.isaias.finance.category_service.data.dto.CategoryCreationResponseDTO;
import com.isaias.finance.category_service.domain.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity <CategoryCreationResponseDTO> createNewCategory (@RequestBody CategoryCreationRequestDTO category, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        return new ResponseEntity <> (
                categoryService.createNewCategory (category, jwt),
                HttpStatus.CREATED);
    }
}