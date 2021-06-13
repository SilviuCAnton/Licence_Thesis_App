package com.silviucanton.easyorder.apigatewayservice.services;

import com.silviucanton.easyorder.apigatewayservice.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    List<String> getAuthoritiesByUsername(String username);

    Optional<Long> getEmployeeIdByUsername(String username);
}
