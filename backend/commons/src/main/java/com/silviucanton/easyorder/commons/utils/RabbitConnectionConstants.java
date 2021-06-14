package com.silviucanton.easyorder.commons.utils;

public class RabbitConnectionConstants {
    public static final String ORDER_EXCHANGE_NAME = "order-saga-exchange-order";
    public static final String ORDER_QUEUE_NAME = "order-saga-queue-order";
    public static final String INVENTORY_EXCHANGE_NAME = "order-saga-exchange-inventory";
    public static final String INVENTORY_QUEUE_NAME = "order-saga-queue-inventory";
    public static final String ORDER_ROUTING_KEY = "order-key";
    public static final String INVENTORY_ROUTING_KEY = "inventory-key";

}
