package com.silviucanton.easyorder.logisticsservice.webservices;

import com.silviucanton.easyorder.commons.dto.TableDTO;
import com.silviucanton.easyorder.logisticsservice.domain.exceptions.TableNotFoundException;
import com.silviucanton.easyorder.logisticsservice.services.TableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/{tableId}")
    public TableDTO getTableById(@PathVariable long tableId) {
        return tableService.getTableById(tableId)
                .orElseThrow(() -> new TableNotFoundException("Table with given id was not found"));
    }

}
