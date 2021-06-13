package com.silviucanton.easyorder.commons.client;

import com.silviucanton.easyorder.commons.dto.EmployeeDTO;
import com.silviucanton.easyorder.commons.dto.EmployeeType;
import com.silviucanton.easyorder.commons.dto.TableDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class LogisticsClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    private final String EMPLOYEES_URL;
    private final String TABLES_URL;

    public LogisticsClient(String logisticsUrl) {
        EMPLOYEES_URL = logisticsUrl + "/employees";
        TABLES_URL = logisticsUrl + "/tables";
    }

    public Optional<WaiterDTO> findWaiterById(long waiterId) {
        ResponseEntity<WaiterDTO> response = restTemplate.getForEntity(EMPLOYEES_URL + "/" + waiterId, WaiterDTO.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    public Optional<TableDTO> findTableByID(long tableID) {
        ResponseEntity<TableDTO> response = restTemplate.getForEntity(TABLES_URL + "/" + tableID, TableDTO.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }
        return Optional.empty();
    }

    public EmployeeDTO createEmployee(String name, String username, String type) {
        EmployeeType employeeType = getEmployeeType(type);
        return restTemplate.postForObject(EMPLOYEES_URL, new EmployeeDTO(0L, name, username, employeeType), EmployeeDTO.class);
    }

    private EmployeeType getEmployeeType(String type) {
        switch (type) {
            case "MANAGER":
                return EmployeeType.MANAGER;
            case "WAITER":
            default:
                return EmployeeType.WAITER;
        }
    }
}
