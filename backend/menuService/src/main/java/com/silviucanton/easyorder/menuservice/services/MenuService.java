package com.silviucanton.easyorder.menuservice.services;

import com.silviucanton.easyorder.commons.dto.MenuSectionDTO;

import java.util.List;

public interface MenuService {
    List<MenuSectionDTO> getMenu();
}
