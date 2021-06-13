package com.silviucanton.easyorder.logisticsservice.services;

import com.silviucanton.easyorder.commons.dto.EmployeeDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;

import java.util.Optional;

public interface EmployeeService {
    Optional<WaiterDTO> getWaiterById(Long id);

    Optional<WaiterDTO> getWaiterByUsername(String username);

    EmployeeDTO saveEmployee(EmployeeDTO employeeToCreate);
}
