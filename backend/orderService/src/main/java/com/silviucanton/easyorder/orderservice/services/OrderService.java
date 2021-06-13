package com.silviucanton.easyorder.orderservice.services;

import com.silviucanton.easyorder.commons.dto.DisplayOrderDTO;
import com.silviucanton.easyorder.commons.dto.OrderDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;

import java.util.List;

public interface OrderService {
    List<DisplayOrderDTO> getOrdersByWaiterIdWithStatus(long WaiterId, boolean isDone);

    DisplayOrderDTO saveOrder(OrderDTO orderDTO, WaiterDTO waiter);

    void updateOrderStatus(Long id, boolean newStatus);
}
