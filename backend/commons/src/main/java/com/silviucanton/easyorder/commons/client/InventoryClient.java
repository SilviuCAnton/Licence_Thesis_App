package com.silviucanton.easyorder.commons.client;

import com.silviucanton.easyorder.commons.dto.MenuItemDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class InventoryClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    private final String ITEMS_URL;

    public InventoryClient(String inventoryUrl) {
        ITEMS_URL = inventoryUrl + "/items";
    }

    public Optional<MenuItemDTO> findMenuItemByID(Long menuItemId) {
        ResponseEntity<MenuItemDTO> response =
                restTemplate.getForEntity(ITEMS_URL + "/" + menuItemId, MenuItemDTO.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    public List<MenuItemDTO> getAllMenuItemsByCategory() {
        ResponseEntity<List<MenuItemDTO>> response =
                restTemplate.exchange(ITEMS_URL + "?byCategory=true", HttpMethod.GET, HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<MenuItemDTO>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return Collections.emptyList();
    }
}
