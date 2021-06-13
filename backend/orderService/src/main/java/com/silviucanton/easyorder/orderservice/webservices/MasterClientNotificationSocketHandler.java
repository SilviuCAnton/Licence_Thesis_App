package com.silviucanton.easyorder.orderservice.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silviucanton.easyorder.commons.dto.MasterClientConnectMessage;
import com.silviucanton.easyorder.commons.dto.TempOrderDTO;
import com.silviucanton.easyorder.commons.dto.WebSocketNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Socket handler responsible of sending notifications to the Master Client.
 */
@Slf4j
public class MasterClientNotificationSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions;
    private final Map<String, WebSocketSession> connectedMasterClients;
    private final ReentrantLock reentrantLock;
    private final ObjectMapper objectMapper;

    public MasterClientNotificationSocketHandler() {
        this.sessions = new CopyOnWriteArrayList<>();
        this.connectedMasterClients = new HashMap<>();
        this.reentrantLock = new ReentrantLock(true);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        this.sessions.add(session);
        super.afterConnectionEstablished(session);
        log.debug("Established new connection. Size: {}", this.sessions.size());
    }

    /**
     * Removes a session from the list and from the Masters' Client associated sessions.
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        this.sessions.remove(session);
        this.connectedMasterClients
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session))
                .findFirst()
                .ifPresent(entry -> {
                    connectedMasterClients.remove(entry.getKey());
                });
        super.afterConnectionClosed(session, status);
    }

    /**
     * Register a new Master Client.
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        MasterClientConnectMessage masterClientConnectMessage =
                this.objectMapper.readValue(message.getPayload(), MasterClientConnectMessage.class);
        this.connectedMasterClients.put(masterClientConnectMessage.getSessionId(), session);
        super.handleTextMessage(session, message);
    }

    /**
     * Notifies the Master Client about a change.
     * Uses synchronization due to possibly multiple requests.
     */
    public void notifyMasterClient(String sessionId, WebSocketNotification<TempOrderDTO> webSocketNotification) {
        try {
            this.reentrantLock.lock();
            this.connectedMasterClients.get(sessionId)
                    .sendMessage(new TextMessage(this.objectMapper.writeValueAsString(webSocketNotification)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.reentrantLock.unlock();
        }
    }
}
