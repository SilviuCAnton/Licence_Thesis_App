package com.silviucanton.easyorder.orderservice.webservices;

import com.silviucanton.easyorder.commons.dto.DisplayOrderDTO;
import com.silviucanton.easyorder.commons.dto.MenuSectionDTO;
import com.silviucanton.easyorder.commons.dto.OrderDTO;
import com.silviucanton.easyorder.commons.dto.ReserveItemsDTO;
import com.silviucanton.easyorder.commons.utils.RabbitConnectionConstants;
import com.silviucanton.easyorder.commons.utils.RabbitConstants;
import com.silviucanton.easyorder.orderservice.services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(OrderService orderService,
                           RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns the order saved with the actual id from the db and server timestamp",
            response = MenuSectionDTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public DisplayOrderDTO saveOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.debug("Entered class = OrderController & method = saveOrder");

        rabbitTemplate.convertAndSend(RabbitConnectionConstants.INVENTORY_EXCHANGE_NAME,
                RabbitConnectionConstants.INVENTORY_ROUTING_KEY,
                new ReserveItemsDTO(RabbitConstants.ORDER_PENDING_STATUS, orderDTO));

        return new DisplayOrderDTO();
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Forbidden - Needs WAITER or MANAGER authority"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "get all the orders for the waiter with the specified id that have the specified status",
            response = OrderDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public List<DisplayOrderDTO> getOrdersForWaiterWithStatus(
            @ApiParam(name = "id", type = "Long", value = "The id of the waiter whose " +
                    "orders are desired", example = "55")
            @RequestParam Long id,
            @ApiParam(name = "isDone", type = "Boolean", value = "The status of the orders that are desired",
                    example = "true")
            @RequestParam(required = false, defaultValue = "false") Boolean isDone) {
        log.debug("Entered class = OrderController & method = getOrdersForWaiterWithStatus");
        return orderService.getOrdersByWaiterIdWithStatus(id, isDone);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Forbidden - Needs WAITER authority"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict - Order is already done"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "mark the order with the specified id as done")
    @PutMapping("/{orderId}/markAsDone")
    public void markOrderAsDone(
            @ApiParam(name = "id", type = "Long", value = "The id of the order to be changed", example = "69")
            @PathVariable Long orderId) {
        log.debug("Entered class = OrderController & method = markOrderAsDone");
        orderService.updateOrderStatus(orderId, true);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Forbidden - Need MANAGER authority"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict - Order already not done"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "mark the order with the specified id as not done")
    @PutMapping("/{orderId}/markAsNotDone")
    public void markOrderAsNotDone(
            @ApiParam(name = "id", type = "Long", value = "The id of the order to be changed", example = "68")
            @PathVariable Long orderId) {
        log.debug("Entered class = OrderController & method = markOrderAsNotDone");
        orderService.updateOrderStatus(orderId, false);
    }
}
