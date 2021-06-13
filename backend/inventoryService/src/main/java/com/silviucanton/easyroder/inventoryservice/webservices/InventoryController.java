package com.silviucanton.easyroder.inventoryservice.webservices;

import com.silviucanton.easyorder.commons.dto.MenuItemDTO;
import com.silviucanton.easyroder.inventoryservice.services.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/items")
@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "updates the menu item and returns the new value",
            response = MenuItemDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("/{id}")
    public MenuItemDTO updateMenuItem(
            @ApiParam(name = "id", type = "Long", value = "The id of the menuItem to be updated", example = "7")
            @PathVariable Long id,
            @ApiParam(name = "menuItemDTO", type = "MenuItemDTO", value = "the MenuItemDTO containing new values")
            @Valid @RequestBody MenuItemDTO menuItemDTO) {
        log.debug("Entered class = MenuController & method = update");
        menuItemDTO.setId(id);
        return inventoryService.updateMenuItem(menuItemDTO);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "saves the menu item and returns it, with an updated id",
            response = MenuItemDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public MenuItemDTO addMenuItem(@ApiParam(name = "menuItemDTO", type = "MenuItemDTO",
            value = "The MenuItemDTO containing the details of the item")
                                   @Valid @RequestBody MenuItemDTO menuItemDTO) {
        log.debug("Entered class = MenuController & method = addMenuItem");
        return inventoryService.addMenuItem(menuItemDTO);
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the menu items paged and sorted depending on the parameters given",
            response = MenuItemDTO.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<MenuItemDTO> getMenuItems(
            @ApiParam(name = "page", type = "Integer", value = "Number of the page", example = "2")
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "size", type = "Integer", value = "The size of one page", example = "5")
            @RequestParam(required = false) Integer size,
            @ApiParam(name = "sortBy", type = "String", value = "sort criteria", example = "name.asc,price.desc")
            @RequestParam(required = false) String sortBy,
            @ApiParam(name = "byCategory", type = "Boolean", value = "Should items be grouped", example = "true, false")
            @RequestParam(required = false) boolean byCategory) {
        log.debug("Entered class = MenuController & method = getMenuItems");
        if (byCategory) {
            return inventoryService.getAllByCategory();
        }
        return inventoryService.getMenuItems(page, size, sortBy);
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the menu item that has the specified id",
            response = MenuItemDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{id}")
    public MenuItemDTO getMenuItemById(
            @ApiParam(name = "id", type = "Long", value = "The id of the requested MenuItem", example = "3")
            @PathVariable Long id) {
        log.debug("Entered class = MenuController & method = getMenuItemById");
        return inventoryService.getMenuItemById(id);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden - Needs manager authority"),
            @ApiResponse(code = 404, message = "Not Found - Specified id does not exist"),
            @ApiResponse(code = 409, message = "Conflict - MenuItem is used in an order"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "deletes the item with the specified id and does not return a value")
    @DeleteMapping("/{id}")
    public void deleteMenuItem(
            @ApiParam(name = "id", type = "Long", value = "The id of the menuItem to be deleted", example = "69")
            @PathVariable Long id) {
        log.debug("Entered class = MenuController & method = deleteMenuItem");
        inventoryService.deleteMenuItem(id);
    }
}