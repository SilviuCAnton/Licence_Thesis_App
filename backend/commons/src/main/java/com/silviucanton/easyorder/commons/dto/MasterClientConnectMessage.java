package com.silviucanton.easyorder.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterClientConnectMessage {
    private String sessionId;
}
