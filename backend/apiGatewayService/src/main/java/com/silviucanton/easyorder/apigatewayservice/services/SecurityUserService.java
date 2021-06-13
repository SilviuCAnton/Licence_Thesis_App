package com.silviucanton.easyorder.apigatewayservice.services;

import com.silviucanton.easyorder.apigatewayservice.dao.UserRepository;
import com.silviucanton.easyorder.apigatewayservice.domain.model.SecurityUser;
import com.silviucanton.easyorder.apigatewayservice.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds an existing user inside the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> loggedUserOpt = userRepository.findByUsername(username);

        User user = loggedUserOpt.orElseThrow(() -> new UsernameNotFoundException("Username was not found."));

        List<String> authorities = user.getAuthoritiesAsStrings();
        Collection<GrantedAuthority> securityAuthorities =
                AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

        return new SecurityUser(username, user.getPassword(), securityAuthorities);
    }
}
