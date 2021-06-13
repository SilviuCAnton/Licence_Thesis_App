package com.silviucanton.easyorder.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

}
