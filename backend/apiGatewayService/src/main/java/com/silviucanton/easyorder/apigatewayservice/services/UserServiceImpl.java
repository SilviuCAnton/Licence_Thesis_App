package com.silviucanton.easyorder.apigatewayservice.services;

import com.silviucanton.easyorder.apigatewayservice.dao.AuthorityRepository;
import com.silviucanton.easyorder.apigatewayservice.dao.UserRepository;
import com.silviucanton.easyorder.apigatewayservice.domain.exceptions.NoValidAuthorityException;
import com.silviucanton.easyorder.apigatewayservice.domain.exceptions.UserAlreadyExistsException;
import com.silviucanton.easyorder.apigatewayservice.domain.model.Authority;
import com.silviucanton.easyorder.apigatewayservice.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Saves a new account into the database
     */
    @Override
    public User saveUser(User user) {
        user.setId(0);
        List<Authority> authorities = authorityRepository.findByNameIn(user.getAuthorities()
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toList()));

        if (authorities.isEmpty()) {
            throw new NoValidAuthorityException("None of the given authorities is valid");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    @Override
    public List<String> getAuthoritiesByUsername(String username) {
        return userRepository.findByUsername(username).map(User::getAuthoritiesAsStrings)
                .orElse(Collections.emptyList());
    }

    @Override public Optional<Long> getEmployeeIdByUsername(String username) {
        return userRepository.findByUsername(username).map(User::getEmployeeId).or(Optional::empty);
    }
}
