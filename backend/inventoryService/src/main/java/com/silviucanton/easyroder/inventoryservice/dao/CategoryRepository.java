package com.silviucanton.easyroder.inventoryservice.dao;

import com.silviucanton.easyroder.inventoryservice.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);
}
