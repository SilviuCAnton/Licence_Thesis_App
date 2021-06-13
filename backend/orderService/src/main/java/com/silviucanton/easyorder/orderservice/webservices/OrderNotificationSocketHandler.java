package com.silviucanton.easyorder.orderservice.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silviucanton.easyorder.commons.dto.DisplayOrderDTO;
import com.silviucanton.easyorder.commons.dto.OrderDTO;
import com.silviucanton.easyorder.commons.dto.WaiterConnectMessage;
import com.silviucanton.easyorder.commons.dto.WebSocketNotification;
import com.silviucanton.easyorder.orderservice.domain.exceptions.WaiterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Socket handler responsible of sending notification to Waiters.
 */
@Slf4j
public class OrderNotificationSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions;
    private final Map<Long, WebSocketSession> connectedWaiters;
    private final Queue<Long> connectedWaitersQueue;
    private final ReentrantLock reentrantLock;
    private final ObjectMapper objectMapper;

    public OrderNotificationSocketHandler() {
        this.sessions = new CopyOnWriteArrayList<>();
        this.connectedWaiters = new HashMap<>();
        this.connectedWaitersQueue = new LinkedList<>();
        this.reentrantLock = new ReentrantLock(true);
        this.objectMapper = new ObjectMapper();

//         TODO: This should be uncommented when running tests
//        connectedWaitersQueue.add(-2L);
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        sessions.add(session);
        super.afterConnectionEstablished(session);
        log.debug("New session to establish, size {}", sessions.size());
    }

    /**
     * Register a new Waiter.
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        WaiterConnectMessage connectMessage = objectMapper.readValue(message.getPayload(), WaiterConnectMessage.class);
        connectedWaiters.put(connectMessage.getWaiterId(), session);
        connectedWaitersQueue.add(connectMessage.getWaiterId());
        super.handleTextMessage(session, message);
    }

    /**
     * Removes a session from the list of all sessions, from the Waiters' associated sessions and from the queue.
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        sessions.remove(session);
        connectedWaiters.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .findFirst()
                .ifPresent(entry -> {
                    connectedWaitersQueue.remove(entry.getKey());
                    connectedWaiters.remove(entry.getKey());
                });
        super.afterConnectionClosed(session, status);
    }

    public void notifyAll(WebSocketNotification<OrderDTO> webSocketNotification) {
        sessions.forEach(session -> {
            try {
                this.reentrantLock.lock();
                System.out.println(webSocketNotification.getEvent());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(webSocketNotification)));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.reentrantLock.unlock();
            }
        });
    }

    /**
     * Notifies the Waiter about a change.
     * Uses synchronization due to possibly multiple requests.
     */
    public void notifyWaiter(Long waiterId, WebSocketNotification<DisplayOrderDTO> webSocketNotification) {
        try {
            this.reentrantLock.lock();
            WebSocketSession waiterSession = connectedWaiters.get(waiterId);
            if (waiterSession == null) {
                throw new WaiterNotFoundException("The waiter with the given id doesn't exist.");
            }
            waiterSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(webSocketNotification)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.reentrantLock.unlock();
        }
    }

    /**
     * Waiter distribution.
     * Don't overwork a waiter, give equal amount of work.
     */
    public Long getNextWaiterId() {
        if (connectedWaitersQueue.isEmpty()) {
            throw new WaiterNotFoundException("The waiter with the given id doesn't exist.");
        }
        Long waiterId = connectedWaitersQueue.remove();
        connectedWaitersQueue.add(waiterId);
        return waiterId;
    }
}
