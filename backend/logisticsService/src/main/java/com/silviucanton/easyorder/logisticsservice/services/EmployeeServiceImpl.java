package com.silviucanton.easyorder.logisticsservice.services;

import com.silviucanton.easyorder.commons.dto.EmployeeDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;
import com.silviucanton.easyorder.logisticsservice.dao.ManagerRepository;
import com.silviucanton.easyorder.logisticsservice.dao.WaiterRepository;
import com.silviucanton.easyorder.logisticsservice.domain.model.Manager;
import com.silviucanton.easyorder.logisticsservice.domain.model.Waiter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final WaiterRepository waiterRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper dtoMapper;

    public EmployeeServiceImpl(WaiterRepository waiterRepository,
                               ManagerRepository managerRepository,
                               ModelMapper dtoMapper) {
        this.waiterRepository = waiterRepository;
        this.managerRepository = managerRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Optional<WaiterDTO> getWaiterById(Long id) {
        return waiterRepository.findById(id).map(waiter -> dtoMapper.map(waiter, WaiterDTO.class));
    }

    @Override public Optional<WaiterDTO> getWaiterByUsername(String username) {
        return waiterRepository.findByUsername(username).map(waiter -> dtoMapper.map(waiter, WaiterDTO.class));
    }

    @Override public EmployeeDTO saveEmployee(EmployeeDTO employeeToCreate) {
        switch(employeeToCreate.getEmployeeType()){
            case MANAGER:
                Manager manager = new Manager();
                manager.setId(employeeToCreate.getId());
                manager.setName(employeeToCreate.getName());
                manager.setUsername(employeeToCreate.getUsername());
                return dtoMapper.map(managerRepository.save(manager), EmployeeDTO.class);
            case WAITER:
                Waiter waiter = new Waiter();
                waiter.setId(employeeToCreate.getId());
                waiter.setName(employeeToCreate.getName());
                waiter.setUsername(employeeToCreate.getUsername());
                return dtoMapper.map(waiterRepository.save(waiter), EmployeeDTO.class);
            default:
                return null;
        }
    }
}
