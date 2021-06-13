package com.silviucanton.easyorder.logisticsservice.dao;

import com.silviucanton.easyorder.logisticsservice.domain.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findByUsername(String username);
}
