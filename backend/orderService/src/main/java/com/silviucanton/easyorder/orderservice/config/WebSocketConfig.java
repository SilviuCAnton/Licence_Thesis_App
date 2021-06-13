package com.silviucanton.easyorder.orderservice.config;

import com.silviucanton.easyorder.orderservice.webservices.MasterClientNotificationSocketHandler;
import com.silviucanton.easyorder.orderservice.webservices.OrderNotificationSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderNotificationHandler(), "/newOrder")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");

        registry.addHandler(masterClientNotificationSocketHandler(), "/newMasterClient")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public OrderNotificationSocketHandler orderNotificationHandler() {
        return new OrderNotificationSocketHandler();
    }

    @Bean
    public MasterClientNotificationSocketHandler masterClientNotificationSocketHandler() {
        return new MasterClientNotificationSocketHandler();
    }
}