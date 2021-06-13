package com.silviucanton.easyorder.logisticsservice.services;

import com.silviucanton.easyorder.commons.dto.TableDTO;
import com.silviucanton.easyorder.logisticsservice.dao.TableRepository;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final ModelMapper dtoMapper;

    public TableServiceImpl(TableRepository tableRepository, ModelMapper dtoMapper) {
        this.tableRepository = tableRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override public Optional<TableDTO> getTableById(long tableId) {
        return tableRepository.findById(tableId).map(table -> dtoMapper.map(table, TableDTO.class));
    }
}
