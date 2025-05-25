package com.isaias.finance.category_service.config;

import com.isaias.finance.category_service.data.entity.Category;
import com.isaias.finance.category_service.data.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            List<String> baseCategories = List.of("Health", "Entertainment", "Transport", "Food");

            for (String name : baseCategories) {
                boolean exists = categoryRepository.existsByNameAndUsernameIsNull(name);
                if (!exists) {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    categoryRepository.save(newCategory);
                }
            }
        };
    }
}