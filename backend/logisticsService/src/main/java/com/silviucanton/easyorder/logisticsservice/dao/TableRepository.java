package com.silviucanton.easyorder.logisticsservice.dao;

import com.silviucanton.easyorder.logisticsservice.domain.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
}
