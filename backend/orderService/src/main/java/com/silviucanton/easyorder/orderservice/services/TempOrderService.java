package com.silviucanton.easyorder.orderservice.services;

import com.silviucanton.easyorder.commons.dto.TempOrderDTO;
import com.silviucanton.easyorder.orderservice.domain.model.TempOrder;

import java.util.List;

public interface TempOrderService {
    TempOrderDTO storeTempOrder(TempOrderDTO tempOrderDTO);

    void deleteTempOrderBySessionId(String sessionId);

    List<TempOrder> getAllBySessionId(String sessionId);

    TempOrderDTO updateTempOrder(TempOrderDTO tempOrderDTO);
}
