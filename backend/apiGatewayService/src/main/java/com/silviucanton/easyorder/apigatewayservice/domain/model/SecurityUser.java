package com.silviucanton.easyorder.apigatewayservice.domain.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends org.springframework.security.core.userdetails.User {
    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public User toAppUser() {
        List<Authority> authorities = new ArrayList<>();
        this.getAuthorities()
                .forEach(grantedAuthority -> authorities.add(new Authority(0L, grantedAuthority.getAuthority())));

        return new User(0, getUsername(), getPassword(), 0L, authorities);
    }
}
