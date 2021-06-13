package com.silviucanton.easyorder.orderservice.webservices;

import com.silviucanton.easyorder.orderservice.services.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/session", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequestMapping("/session")
@RestController
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "returns a newly generated, non-existent sessionId for a new shared session",
            response = String.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/newSessionId")
    public String getNewSessionId() {
        return sessionService.generateNewSessionId();
    }
}
