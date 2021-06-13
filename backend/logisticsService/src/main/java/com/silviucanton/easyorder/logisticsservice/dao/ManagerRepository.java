package com.silviucanton.easyorder.logisticsservice.dao;

import com.silviucanton.easyorder.logisticsservice.domain.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
