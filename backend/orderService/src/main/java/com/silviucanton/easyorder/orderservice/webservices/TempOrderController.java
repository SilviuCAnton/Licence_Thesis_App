package com.silviucanton.easyorder.orderservice.webservices;

import com.silviucanton.easyorder.commons.dto.Payload;
import com.silviucanton.easyorder.commons.dto.TempOrderDTO;
import com.silviucanton.easyorder.commons.dto.WebSocketEvents;
import com.silviucanton.easyorder.commons.dto.WebSocketNotification;
import com.silviucanton.easyorder.orderservice.domain.model.TempOrder;
import com.silviucanton.easyorder.orderservice.services.TempOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "/tempOrders", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/tempOrders")
@RestController
public class TempOrderController {

    private final TempOrderService tempOrderService;
    private final MasterClientNotificationSocketHandler masterClientNotificationSocketHandler;

    public TempOrderController(TempOrderService tempOrderService,
                               MasterClientNotificationSocketHandler masterClientNotificationSocketHandler) {
        this.tempOrderService = tempOrderService;
        this.masterClientNotificationSocketHandler = masterClientNotificationSocketHandler;
    }

    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "Store the tempOrder and retrieve the same one with the id set by the server",
            response = TempOrder.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TempOrderDTO storeTempOrder(@Valid @RequestBody TempOrderDTO tempOrderDTO) {
        TempOrderDTO storeResult = tempOrderService.storeTempOrder(tempOrderDTO);

        //TODO: comment this line when testing
        masterClientNotificationSocketHandler
                .notifyMasterClient(storeResult.getSessionId(), new WebSocketNotification<>(
                        WebSocketEvents.NEW_TEMP_ORDER_EVENT, new Payload<>("", storeResult)));

        return storeResult;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "Update the tempOrder and receive the updated version back",
            response = TempOrderDTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping
    public TempOrderDTO updateTempOrder(@Valid @RequestBody TempOrderDTO tempOrderDTO) {
        TempOrderDTO updateResult = tempOrderService.updateTempOrder(tempOrderDTO);

        //TODO: comment this line when testing
        masterClientNotificationSocketHandler
                .notifyMasterClient(updateResult.getSessionId(), new WebSocketNotification<>(
                        WebSocketEvents.NEW_TEMP_ORDER_EVENT, new Payload<>("", updateResult)));

        return updateResult;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "Delete all the tempOrders having the specified sessionId")
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTempOrder(@ApiParam(name = "sessionId", type = "String",
            value = "The id of a temporary order session", example = "SESSION:3") @PathVariable String sessionId) {
        tempOrderService.deleteTempOrderBySessionId(sessionId);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "Get all the tempOrders having the specified sessionId",
            response = TempOrder.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TempOrder> getTempOrdersBySessionId(@ApiParam(name = "sessionId", type = "String",
            value = "The id of a temporary order session", example = "SESSION:3") @PathVariable String sessionId) {
        return tempOrderService.getAllBySessionId(sessionId);
    }

}
