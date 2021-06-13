package com.silviucanton.easyorder.logisticsservice.services;

import com.silviucanton.easyorder.commons.dto.TableDTO;

import java.util.Optional;

public interface TableService {

    Optional<TableDTO> getTableById(long tableId);

}
