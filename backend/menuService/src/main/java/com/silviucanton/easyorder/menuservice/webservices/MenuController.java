package com.silviucanton.easyorder.menuservice.webservices;

import com.silviucanton.easyorder.commons.dto.MenuSectionDTO;
import com.silviucanton.easyorder.menuservice.services.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/menu")
@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the menu items encapsulated into sections", response = MenuSectionDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<MenuSectionDTO> getMenu() {
        log.debug("Entered class = MenuController & method = getMenu");
        return menuService.getMenu();
    }
}