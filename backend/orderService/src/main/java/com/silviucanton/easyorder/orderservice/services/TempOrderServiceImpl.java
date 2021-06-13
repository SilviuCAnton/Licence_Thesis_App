package com.silviucanton.easyorder.orderservice.services;

import com.silviucanton.easyorder.commons.dto.TempOrderDTO;
import com.silviucanton.easyorder.orderservice.dao.TempOrderRepository;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TempOrderConflictException;
import com.silviucanton.easyorder.orderservice.domain.exceptions.TempOrderNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.model.TempOrder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempOrderServiceImpl implements TempOrderService {

    private final TempOrderRepository tempOrderRepository;
    private final ModelMapper dtoMapper;

    public TempOrderServiceImpl(TempOrderRepository tempOrderRepository, ModelMapper dtoMapper) {
        this.tempOrderRepository = tempOrderRepository;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Save a new temporary order or throw if the guest name of the client already exists in the session.
     */
    @Override
    public TempOrderDTO storeTempOrder(TempOrderDTO tempOrderDTO) {
        if (tempOrderRepository.existsByNicknameAndSessionId(tempOrderDTO.getNickname(), tempOrderDTO.getSessionId())) {
            throw new TempOrderConflictException("The name is already present in other tempOrder!");
        }

        TempOrder tempOrder = new TempOrder();
        tempOrder.setId(0);
        tempOrder.setSessionId(tempOrderDTO.getSessionId());
        tempOrder.setNickname(tempOrderDTO.getNickname());
        tempOrder.setMenuItemIds(tempOrderDTO.getMenuItemIds());

        return dtoMapper.map(tempOrderRepository.save(tempOrder), TempOrderDTO.class);
    }

    @Override
    public void deleteTempOrderBySessionId(String sessionId) {
        List<TempOrder> tempOrders = tempOrderRepository.getAllBySessionId(sessionId);
        for (TempOrder tempOrder : tempOrders) {
            tempOrderRepository.delete(tempOrder);
        }
    }

    @Override
    public List<TempOrder> getAllBySessionId(String sessionId) {
        return tempOrderRepository.getAllBySessionId(sessionId);
    }

    /**
     * Update a temporary order or throw an error if it does not exist.
     */
    @Override
    public TempOrderDTO updateTempOrder(TempOrderDTO tempOrderDTO) {
        TempOrder tempOrder = new TempOrder();
        tempOrder.setSessionId(tempOrderDTO.getSessionId());
        tempOrder.setNickname(tempOrderDTO.getNickname());
        tempOrder.setMenuItemIds(tempOrderDTO.getMenuItemIds());

        tempOrder.setId(tempOrderRepository
                .findBySessionIdAndNickname(tempOrderDTO.getSessionId(), tempOrder.getNickname()).map(TempOrder::getId)
                .orElseThrow(() -> new TempOrderNotFoundException(
                        String.format(
                                "Temporary Order for the session %s with the nickname %s does not exist!",
                                tempOrderDTO.getSessionId(),
                                tempOrderDTO.getNickname()
                        ))));

        return dtoMapper.map(tempOrderRepository.save(tempOrder), TempOrderDTO.class);
    }
}
