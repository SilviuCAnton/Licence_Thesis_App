package com.silviucanton.easyorder.logisticsservice.webservices;

import com.silviucanton.easyorder.commons.dto.EmployeeDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;
import com.silviucanton.easyorder.logisticsservice.domain.exceptions.WaiterNotFoundException;
import com.silviucanton.easyorder.logisticsservice.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("employees")
@Api(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Forbidden - Needs WAITER authority"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "Get the id of the waiter with the given username",
            response = Long.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/idByUsername")
    public Long getWaiterIdByUsername(
            @ApiParam(name = "username", type = "String",
                    value = "The username of the waiter whose id is to be obtained",
                    example = "waiter")
            @RequestParam(value = "username") String username) {
        Optional<WaiterDTO> waiterOpt = employeeService.getWaiterByUsername(username);
        return waiterOpt.map(WaiterDTO::getId)
                .orElseThrow(() -> new WaiterNotFoundException("Waiter with given username was not found"));
    }

    @PostMapping
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeToCreate){
        return employeeService.saveEmployee(employeeToCreate);
    }
}
