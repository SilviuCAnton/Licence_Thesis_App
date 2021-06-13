package com.silviucanton.easyorder.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketNotification<T> {
    private String event;
    private Payload<T> payload;
}