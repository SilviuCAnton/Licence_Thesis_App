package com.silviucanton.easyorder.orderservice.webservices;

import com.silviucanton.easyorder.commons.client.LogisticsClient;
import com.silviucanton.easyorder.commons.dto.DisplayOrderDTO;
import com.silviucanton.easyorder.commons.dto.Payload;
import com.silviucanton.easyorder.commons.dto.ReserveItemsDTO;
import com.silviucanton.easyorder.commons.dto.TempOrderDTO;
import com.silviucanton.easyorder.commons.dto.WaiterDTO;
import com.silviucanton.easyorder.commons.dto.WebSocketEvents;
import com.silviucanton.easyorder.commons.dto.WebSocketNotification;
import com.silviucanton.easyorder.commons.utils.RabbitConnectionConstants;
import com.silviucanton.easyorder.commons.utils.RabbitConstants;
import com.silviucanton.easyorder.orderservice.domain.exceptions.WaiterNotFoundException;
import com.silviucanton.easyorder.orderservice.domain.model.TempOrder;
import com.silviucanton.easyorder.orderservice.services.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RabbitOrderSagaReceiver {

    private final OrderService orderService;
    private final LogisticsClient logisticsClient;
    private final OrderNotificationSocketHandler orderNotificationHandler;
    private final MasterClientNotificationSocketHandler masterClientNotificationSocketHandler;
    private final RabbitTemplate rabbitTemplate;

    public RabbitOrderSagaReceiver(OrderService orderService,
                                   LogisticsClient logisticsClient,
                                   OrderNotificationSocketHandler orderNotificationHandler,
                                   MasterClientNotificationSocketHandler masterClientNotificationSocketHandler,
                                   RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.logisticsClient = logisticsClient;
        this.orderNotificationHandler = orderNotificationHandler;
        this.masterClientNotificationSocketHandler = masterClientNotificationSocketHandler;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void receiveMessage(ReserveItemsDTO reserveItemsDTO) {
        switch (reserveItemsDTO.getStatus()) {
            case RabbitConstants.RESERVE_SUCCESS:
                try {
                    WaiterDTO waiter = logisticsClient.findWaiterById(orderNotificationHandler.getNextWaiterId())
                            .orElseThrow(
                                    () -> new WaiterNotFoundException("The waiter with the given id doesn't exist."));

                    DisplayOrderDTO displayOrderDTO = orderService.saveOrder(reserveItemsDTO.getOrder(), waiter);

                    orderNotificationHandler
                            .notifyWaiter(waiter.getId(), new WebSocketNotification<>(WebSocketEvents.NEW_ORDER_EVENT,
                                    new Payload<>("", displayOrderDTO)));

                    masterClientNotificationSocketHandler.notifyMasterClient(reserveItemsDTO.getOrder().getSessionId(),
                            new WebSocketNotification<>(WebSocketEvents.PLACED_ORDER_EVENT,
                                    new Payload<>("", new TempOrderDTO(0L, WebSocketEvents.PLACED_ORDER_EVENT,
                                            Collections.emptyList(), reserveItemsDTO.getOrder().getSessionId()))));

                } catch (Exception ex) {
                    rabbitTemplate.convertAndSend(RabbitConnectionConstants.EXCHANGE_NAME,
                            RabbitConnectionConstants.INVENTORY_ROUTING_KEY,
                            new ReserveItemsDTO(RabbitConstants.ORDER_FAILED_STATUS, reserveItemsDTO.getOrder()));

                    masterClientNotificationSocketHandler.notifyMasterClient(reserveItemsDTO.getOrder().getSessionId(),
                            new WebSocketNotification<>(WebSocketEvents.FAILED_ORDER_EVENT,
                                    new Payload<>("", new TempOrderDTO(0L, WebSocketEvents.FAILED_ORDER_EVENT,
                                            Collections.emptyList(), reserveItemsDTO.getOrder().getSessionId()))));
                }
                break;
            case RabbitConstants.RESERVE_FAIL:
                masterClientNotificationSocketHandler.notifyMasterClient(reserveItemsDTO.getOrder().getSessionId(),
                        new WebSocketNotification<>(WebSocketEvents.FAILED_ORDER_EVENT,
                                new Payload<>("", new TempOrderDTO(0L, WebSocketEvents.FAILED_ORDER_EVENT,
                                        Collections.emptyList(), reserveItemsDTO.getOrder().getSessionId()))));
                break;
            default:
                break;
        }
    }

}
