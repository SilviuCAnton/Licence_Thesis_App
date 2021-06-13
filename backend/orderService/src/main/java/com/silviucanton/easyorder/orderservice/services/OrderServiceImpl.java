package com.silviucanton.easyorder.orderservice.services;

import com.silviucanton.easyorder.commons.client.InventoryClient;
import com.silviucanton.easyorder.commons.client.LogisticsClient;
import com.silviucanton.easyorder.commons.dto.DisplayOrderDTO;
import com.silviucanton.easyorder.commons.dto.OrderDTO;
import com.silviucanton.easyorder.commons.dto.TableDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;
import com.silviucanton.easyorder.orderservice.dao.OrderRepository;
import com.silviucanton.easyorder.orderservice.domain.exceptions.MenuItemNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.OrderNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.SameOrderStatusException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TableNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.WaiterNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final LogisticsClient logisticsClient;
    private final InventoryClient inventoryClient;
    private final ModelMapper dtoMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            LogisticsClient logisticsClient,
                            InventoryClient inventoryClient,
                            ModelMapper dtoMapper) {
        this.orderRepository = orderRepository;
        this.logisticsClient = logisticsClient;
        this.inventoryClient = inventoryClient;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Get all orders handled by a waiter with a certain status or throw an error if the waiter does not exist.
     */
    @Override
    public List<DisplayOrderDTO> getOrdersByWaiterIdWithStatus(long waiterId, boolean isDone) {
        if (logisticsClient.findWaiterById(waiterId).isPresent()) {
            return orderRepository.findAllByWaiterIdAndDone(waiterId, isDone).stream()
                    .map(order -> dtoMapper.map(order, DisplayOrderDTO.class))
                    .collect(Collectors.toList());
        }
        throw new WaiterNotFoundException("The waiter with the given id doesn't exist.");
    }

    /**
     * Assign a order to a waiter.
     * Check if the table id is valid.
     * Check if all items for the order exists in the database.
     */
    @Override
    public DisplayOrderDTO saveOrder(OrderDTO orderDTO, WaiterDTO waiterDTO) {
        Optional<TableDTO> foundTable = logisticsClient.findTableByID(orderDTO.getTableId());
        if (foundTable.isEmpty()) {
            throw new TableNotFoundException("The table could not be found!");
        }

        orderDTO.getMenuItemIds()
                .stream()
                .map(inventoryClient::findMenuItemByID)
                .forEach(optMenuItem -> optMenuItem
                        .orElseThrow(() -> new MenuItemNotFoundException("The menu item could not be found!")));

        Order order = new Order(
                0,
                orderDTO.getComments(),
                LocalDateTime.now(),
                false,
                foundTable.get().getId(),
                waiterDTO.getId(),
                orderDTO.getMenuItemIds()
        );

        order.setOrderDate(LocalDateTime.now());
        return dtoMapper.map(orderRepository.save(order), DisplayOrderDTO.class);
    }

    /**
     * Change an order status or throw an error if the status is already set or if the order does not exist.
     */
    public void updateOrderStatus(Long id, boolean newStatus) {
        orderRepository.findById(id).ifPresentOrElse(foundOrder -> {
            if (foundOrder.isDone() == newStatus) {
                throw new SameOrderStatusException("The order already has this status");
            }
            foundOrder.setDone(newStatus);
            orderRepository.save(foundOrder);
        }, () -> {
            throw new OrderNotFoundException("The order with the given ID was not found!");
        });
    }
}
