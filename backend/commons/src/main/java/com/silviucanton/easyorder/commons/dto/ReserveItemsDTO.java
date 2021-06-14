package com.silviucanton.easyorder.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveItemsDTO {
    private String status;
    private OrderDTO order;
}
