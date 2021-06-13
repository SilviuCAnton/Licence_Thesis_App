package com.silviucanton.easyorder.menuservice.services;


import com.silviucanton.easyorder.commons.client.InventoryClient;
import com.silviucanton.easyorder.commons.dto.DisplayMenuItemDTO;
import com.silviucanton.easyorder.commons.dto.MenuItemDTO;
import com.silviucanton.easyorder.commons.dto.MenuSectionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private final ModelMapper dtoMapper;
    private final InventoryClient inventoryClient;

    public MenuServiceImpl(ModelMapper dtoMapper, InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Returns a list of Menu Section DTO grouped by category
     */
    @Override
    public List<MenuSectionDTO> getMenu() {
        List<MenuItemDTO> menuItems = inventoryClient.getAllMenuItemsByCategory();

        Map<String, List<MenuItemDTO>> itemsGroupedByCategory = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItemDTO::getCategory));

        return createMenu(itemsGroupedByCategory);
    }

    /**
     * Map Database Entity to DTO.
     */
    public List<MenuSectionDTO> createMenu(Map<String, List<MenuItemDTO>> itemsGroupedByCategory) {
        List<MenuSectionDTO> menu = new ArrayList<>();

        for (Map.Entry<String, List<MenuItemDTO>> keyValuePair : itemsGroupedByCategory.entrySet()) {
            List<DisplayMenuItemDTO> menuItemDTOs = keyValuePair.getValue()
                    .stream()
                    .map(menuItemDTO -> dtoMapper.map(menuItemDTO, DisplayMenuItemDTO.class))
                    .collect(Collectors.toList());

            MenuSectionDTO menuSectionDTO = new MenuSectionDTO(keyValuePair.getKey(), menuItemDTOs);
            menu.add(menuSectionDTO);
        }

        return menu;
    }
}
