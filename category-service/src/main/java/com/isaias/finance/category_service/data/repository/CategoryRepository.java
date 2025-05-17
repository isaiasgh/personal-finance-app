package com.isaias.finance.category_service.data.repository;

import com.isaias.finance.category_service.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository <Long, Category> {
}