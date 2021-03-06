package com.silviucanton.easyorder.menuservice.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.silviucanton.easyorder.commons.client.InventoryClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public InventoryClient inventoryClient(EurekaClient discoveryClient) {
        InstanceInfo serviceInfo = discoveryClient.getNextServerFromEureka("INVENTORY-SERVICE", false);
        System.out.println(serviceInfo.getHomePageUrl());
        return new InventoryClient(serviceInfo.getHomePageUrl());
    }

}
