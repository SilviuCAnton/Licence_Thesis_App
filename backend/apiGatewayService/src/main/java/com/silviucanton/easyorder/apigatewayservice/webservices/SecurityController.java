package com.silviucanton.easyorder.apigatewayservice.webservices;

import com.silviucanton.easyorder.apigatewayservice.domain.model.Authority;
import com.silviucanton.easyorder.apigatewayservice.domain.model.User;
import com.silviucanton.easyorder.apigatewayservice.services.UserService;
import com.silviucanton.easyorder.commons.client.LogisticsClient;
import com.silviucanton.easyorder.commons.dto.EmployeeDTO;
import com.silviucanton.easyorder.commons.dto.LoginDTO;
import com.silviucanton.easyorder.commons.dto.SignUpDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class SecurityController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final LogisticsClient logisticsClient;

    public SecurityController(UserService userService, PasswordEncoder passwordEncoder,
                              LogisticsClient logisticsClient) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.logisticsClient = logisticsClient;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 406, message = "Not Acceptable - No valid authority"),
            @ApiResponse(code = 409, message = "Conflict - Username already exists"),
            @ApiResponse(code = 500, message = "System Error")
    })
    @ApiOperation(value = "creates an user account", response = String.class, produces = MediaType.TEXT_HTML_VALUE)
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@ApiParam(name = "signUpData", value = "Account data for the new user")
                                         @Valid @RequestBody SignUpDTO signUpData) {

        List<String> authorities = signUpData.getAuthorities();
        authorities = authorities == null ? Collections.emptyList() : authorities;

        String type = authorities.stream().findFirst().orElse("WAITER");
        EmployeeDTO employee = logisticsClient.createEmployee(signUpData.getName(), signUpData.getUsername(), type);

        User user = new User(0L,
                signUpData.getUsername(),
                passwordEncoder.encode(signUpData.getPassword()),
                employee.getId(),
                authorities.stream()
                        .map(authority -> new Authority(0L, authority))
                        .collect(Collectors.toList()));

        userService.saveUser(user);
        return ResponseEntity.ok("Account created successfully");
    }

    @PostMapping("/login")
    @ApiOperation(value = "logs in the user and returns his employee id", response = Long.class,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Long> login(@ApiParam(name = "loginData") LoginDTO loginDto, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Long id = userService.getEmployeeIdByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("A user with given username does not exist"));
        return ResponseEntity.ok(id);
    }
}
