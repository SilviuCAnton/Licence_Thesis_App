package com.silviucanton.easyroder.inventoryservice.dao;

import com.silviucanton.easyroder.inventoryservice.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<Item, Long> {
    List<Item> getAllByCategoryNotNull();
}
