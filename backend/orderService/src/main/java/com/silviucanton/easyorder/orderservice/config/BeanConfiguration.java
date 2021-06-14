package com.silviucanton.easyorder.orderservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.silviucanton.easyorder.commons.client.InventoryClient;
import com.silviucanton.easyorder.commons.client.LogisticsClient;
import com.silviucanton.easyorder.commons.utils.RabbitConnectionConstants;
import com.silviucanton.easyorder.orderservice.webservices.RabbitOrderSagaReceiver;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    static final String topicExchangeName = RabbitConnectionConstants.ORDER_EXCHANGE_NAME;

    static final String queueName = RabbitConnectionConstants.ORDER_QUEUE_NAME;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConnectionConstants.ORDER_ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitOrderSagaReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LogisticsClient logisticsClient(EurekaClient discoveryClient) {
        InstanceInfo serviceInfo = discoveryClient.getNextServerFromEureka("LOGISTICS-SERVICE", false);
        System.out.println(serviceInfo.getHomePageUrl());
        return new LogisticsClient(serviceInfo.getHomePageUrl());
    }

    @Bean
    public InventoryClient inventoryClient(EurekaClient discoveryClient) {
        InstanceInfo serviceInfo = discoveryClient.getNextServerFromEureka("INVENTORY-SERVICE", false);
        System.out.println(serviceInfo.getHomePageUrl());
        return new InventoryClient(serviceInfo.getHomePageUrl());
    }
}
