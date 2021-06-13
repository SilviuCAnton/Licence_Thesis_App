package com.silviucanton.easyroder.inventoryservice.services;

import com.silviucanton.easyorder.commons.dto.MenuItemDTO;

import java.util.List;

public interface InventoryService {
    MenuItemDTO updateMenuItem(MenuItemDTO menuItem);

    MenuItemDTO addMenuItem(MenuItemDTO menuItemDTO);

    List<MenuItemDTO> getMenuItems(Integer page, Integer size, String sortBy);

    MenuItemDTO getMenuItemById(Long id);

    void deleteMenuItem(Long id);

    List<MenuItemDTO> getAllByCategory();
}
