package com.silviucanton.easyroder.inventoryservice.services;

import com.silviucanton.easyorder.commons.dto.MenuItemDTO;
import com.silviucanton.easyroder.inventoryservice.dao.CategoryRepository;
import com.silviucanton.easyroder.inventoryservice.dao.MenuItemRepository;
import com.silviucanton.easyroder.inventoryservice.domain.exceptions.MenuItemNotFoundException;
import com.silviucanton.easyroder.inventoryservice.domain.exceptions.MenuItemUsedByOrderException;
import com.silviucanton.easyroder.inventoryservice.domain.model.Category;
import com.silviucanton.easyroder.inventoryservice.domain.model.Item;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper dtoMapper;

    public InventoryServiceImpl(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository,
                                ModelMapper dtoMapper) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Update a Menu Item. Check if it exists in the Database.
     * The category gets created if it is new.
     */
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItem) {
        if (menuItemRepository.findById(menuItem.getId()).isEmpty()) {
            throw new MenuItemNotFoundException("The menu item with the given ID was not found!");
        }

        Optional<Category> categoryOpt = categoryRepository.findByName(menuItem.getCategory());
        final Item newItem = new Item(menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPhotoPath(),
                menuItem.isAvailable(),
                null, 5);

        categoryOpt.ifPresentOrElse(newItem::setCategory,
                () -> newItem.setCategory(categoryRepository.save(new Category(0L, menuItem.getCategory()))));

        return dtoMapper.map(menuItemRepository.save(newItem), MenuItemDTO.class);
    }

    /**
     * Save a Menu Item.
     * The category gets created if it is new.
     */
    @Override
    public MenuItemDTO addMenuItem(MenuItemDTO menuItemDTO) {
        Optional<Category> categoryOpt = categoryRepository.findByName(menuItemDTO.getCategory());
        final Item newItem = new Item(
                0L,
                menuItemDTO.getName(),
                menuItemDTO.getDescription(),
                menuItemDTO.getPrice(),
                menuItemDTO.getPhotoPath(),
                menuItemDTO.isAvailable(),
                null, 5);
        categoryOpt.ifPresentOrElse(newItem::setCategory,
                () -> newItem.setCategory(categoryRepository.save(new Category(0L, menuItemDTO.getCategory()))));

        return dtoMapper.map(menuItemRepository.save(newItem), MenuItemDTO.class);
    }

    /**
     * Get menu items paged.
     * If size is not specified, all table entities are returned.
     * The items are sorted if specified.
     */
    @Override
    public List<MenuItemDTO> getMenuItems(Integer page, Integer size, String sortBy) {
        Optional<Integer> pageOptional = Optional.ofNullable(page);
        Optional<Integer> sizeOptional = Optional.ofNullable(size);
        Optional<String> sortByOptional = Optional.ofNullable(sortBy);

        if (pageOptional.isEmpty() && sizeOptional.isPresent()) {
            throw new IllegalArgumentException("While page is null size cannot have a value");
        }

        if (pageOptional.isPresent() && sizeOptional.isEmpty()) {
            sizeOptional = Optional.of(5);
        }

        Pageable pageable = PageRequest.of(
                pageOptional.orElse(0),
                sizeOptional.orElse(Integer.MAX_VALUE),
                sortByOptional.map(sort -> Sort.by(getSorting(sort))).orElse(Sort.unsorted())
        );
        return menuItemRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(item -> dtoMapper.map(item, MenuItemDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Maps a string to possible sorting patterns.
     * E.g.: name.asc creates a Sort By Name Ascending Query.
     */
    public List<Sort.Order> getSorting(String sortBy) {
        return Arrays.stream(sortBy.split(","))
                .map(item -> item.split("\\."))
                .map(item -> new Sort.Order(Sort.Direction.fromString(item[1]), item[0]))
                .collect(Collectors.toList());

    }

    /**
     * Return an item by id or throw error if it does not exist.
     */
    @Override
    public MenuItemDTO getMenuItemById(Long id) {
        Optional<Item> menuItemOpt = menuItemRepository.findById(id);
        return menuItemOpt
                .map(item -> dtoMapper.map(item, MenuItemDTO.class))
                .orElseThrow(() -> new MenuItemNotFoundException("The menu item with the given ID was not found!"));
    }

    /**
     * Delete an item or throw an error if it is does not exist or if it is linked to an order.
     */
    @Override
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemNotFoundException("The menu item with the given ID was not found!");
        }
        try {
            menuItemRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new MenuItemUsedByOrderException("The menu item with the given ID was ordered and can't be deleted!");
        }
    }

    @Override public List<MenuItemDTO> getAllByCategory() {
        return menuItemRepository.getAllByCategoryNotNull().stream()
                .map(menuItem -> dtoMapper.map(menuItem, MenuItemDTO.class)).collect(Collectors.toList());
    }
}
