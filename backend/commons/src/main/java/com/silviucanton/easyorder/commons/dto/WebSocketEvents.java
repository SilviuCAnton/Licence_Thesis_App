package com.silviucanton.easyorder.commons.dto;

import java.io.Serializable;

public class WebSocketEvents implements Serializable {

    public static String NEW_ORDER_EVENT = "NEW_ORDER";
    public static String NEW_CLIENT_EVENT = "NEW_CLIENT_EVENT";
    public static String NEW_TEMP_ORDER_EVENT = "NEW_TEMP_ORDER_EVENT";
    public static String PLACED_ORDER_EVENT = "PLACED_ORDER_EVENT";
    public static String FAILED_ORDER_EVENT = "FAILED_ORDER_EVENT";
}
