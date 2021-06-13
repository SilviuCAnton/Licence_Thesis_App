package com.silviucanton.easyorder.orderservice.services;

import com.silviucanton.easyorder.orderservice.dao.TempOrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {

    private final TempOrderRepository tempOrderRepository;

    public SessionServiceImpl(TempOrderRepository tempOrderRepository) {
        this.tempOrderRepository = tempOrderRepository;
    }

    /**
     * Generate an unique session id across the database.
     */
    @Override public String generateNewSessionId() {
        String sessionId = "";
        boolean found = false;
        while (!found) {
            sessionId = UUID.randomUUID().toString();
            if (!tempOrderRepository.existsBySessionId(sessionId)) {
                found = true;
            }
        }
        return sessionId;
    }
}
