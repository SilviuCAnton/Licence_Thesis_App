package com.silviucanton.easyorder.commons.utils;

public class RabbitConnectionConstants {
    public static final String EXCHANGE_NAME = "order-saga-exchange";
    public static final String QUEUE_NAME = "order-saga-queue";
    public static final String ORDER_ROUTING_KEY = "order-key";
    public static final String INVENTORY_ROUTING_KEY = "inventory-key";
}
