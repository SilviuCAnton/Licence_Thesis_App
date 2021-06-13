package com.silviucanton.easyorder.orderservice.dao;

import com.silviucanton.easyorder.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByWaiterIdAndDone(long id, boolean done);
}
