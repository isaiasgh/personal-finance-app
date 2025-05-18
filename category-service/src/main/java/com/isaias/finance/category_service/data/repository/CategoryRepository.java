package com.isaias.finance.category_service.data.repository;

import com.isaias.finance.category_service.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long> {
    boolean existsByNameAndUsernameIsNull(String name);

    boolean existsByNameAndUsername(String name, String username);

    Optional<Category> findByName(String categoryName);

    List<Category> findCategoriesByUsername (String username);
}