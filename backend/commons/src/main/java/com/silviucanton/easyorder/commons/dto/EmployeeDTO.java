package com.silviucanton.easyorder.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private long id;
    private String name;
    private String username;
    private EmployeeType employeeType;
}
