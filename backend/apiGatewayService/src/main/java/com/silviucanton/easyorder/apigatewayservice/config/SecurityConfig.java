package com.silviucanton.easyorder.apigatewayservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.silviucanton.easyorder.apigatewayservice.config.filters.AuthenticationFilter;
import com.silviucanton.easyorder.apigatewayservice.config.filters.AuthorizationFilter;
import com.silviucanton.easyorder.apigatewayservice.services.UserService;
import com.silviucanton.easyorder.commons.client.LogisticsClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public SecurityConfig(@Qualifier("securityUserService") UserDetailsService userDetailsService,
                          UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public LogisticsClient logisticsClient(EurekaClient discoveryClient) {
        InstanceInfo serviceInfo = discoveryClient.getNextServerFromEureka("LOGISTICS-SERVICE", false);
        System.out.println(serviceInfo.getHomePageUrl());
        return new LogisticsClient(serviceInfo.getHomePageUrl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/order-service/orders/*/markAsDone").access("hasAuthority('WAITER')")
                .antMatchers(HttpMethod.PUT, "/order-service/orders/*/markAsNotDone").access("hasAuthority('MANAGER')")
                .antMatchers(HttpMethod.GET, "/order-service/orders").access("hasAnyAuthority('MANAGER', 'WAITER')")
                .antMatchers(HttpMethod.POST, "/order-service/orders").permitAll()
                .antMatchers("/order-service/tempOrders/**", "/order-service/tempOrders").permitAll()
                .antMatchers("/order-service/session/**").permitAll()
                .antMatchers("/menu-service/**").permitAll()
                .antMatchers("/payment-service/**").permitAll()
                .antMatchers("/logistics-service/**").permitAll()
                .antMatchers("/inventory-service/**").permitAll()
                .and().csrf().disable()
                .cors()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), userService))
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configuration for allowing cross origin support
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
