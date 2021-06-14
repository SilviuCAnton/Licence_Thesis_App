package com.silviucanton.easyroder.inventoryservice.webservices;

import com.silviucanton.easyorder.commons.dto.ReserveItemsDTO;
import com.silviucanton.easyorder.commons.utils.RabbitConnectionConstants;
import com.silviucanton.easyorder.commons.utils.RabbitConstants;
import com.silviucanton.easyroder.inventoryservice.dao.MenuItemRepository;
import com.silviucanton.easyroder.inventoryservice.domain.model.Item;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitOrderSagaReceiver {

    private final MenuItemRepository inventoryRepository;
    private final RabbitTemplate rabbitTemplate;

    public RabbitOrderSagaReceiver(MenuItemRepository inventoryRepository,
                                   RabbitTemplate rabbitTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void receiveMessage(ReserveItemsDTO reserveItemsDTO) {
        List<Item> allById = inventoryRepository.findAllById(reserveItemsDTO.getOrder().getMenuItemIds());
        System.out.println("Received message");

        switch (reserveItemsDTO.getStatus()) {
            case RabbitConstants.ORDER_PENDING_STATUS:
                System.out.println("In ORDER_PENDING");
                if (allById.stream().anyMatch(item -> item.getQuantity() == 0)) {
                    rabbitTemplate.convertAndSend(RabbitConnectionConstants.ORDER_EXCHANGE_NAME,
                            RabbitConnectionConstants.ORDER_ROUTING_KEY,
                            new ReserveItemsDTO(RabbitConstants.RESERVE_FAIL, reserveItemsDTO.getOrder()));
                } else {
                    allById.forEach(item -> {
                        item.setQuantity(item.getQuantity() - 1);
                        inventoryRepository.save(item);
                    });
                    rabbitTemplate.convertAndSend(RabbitConnectionConstants.ORDER_EXCHANGE_NAME,
                            RabbitConnectionConstants.ORDER_ROUTING_KEY,
                            new ReserveItemsDTO(RabbitConstants.RESERVE_SUCCESS, reserveItemsDTO.getOrder()));
                }
                break;
            case RabbitConstants.ORDER_FAILED_STATUS:
                allById.forEach(item -> {
                    item.setQuantity(item.getQuantity() + 1);
                    inventoryRepository.save(item);
                });
                break;
            default:
                break;
        }
    }
}
