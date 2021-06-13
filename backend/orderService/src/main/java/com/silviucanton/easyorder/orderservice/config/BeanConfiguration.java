package com.silviucanton.easyorder.orderservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.silviucanton.easyorder.commons.client.InventoryClient;
import com.silviucanton.easyorder.commons.client.LogisticsClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

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
