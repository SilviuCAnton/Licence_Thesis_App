package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayOrderDTO implements Serializable {
    @ApiModelProperty(required = true, example = "9", value = "id")
    @NotNull(message = "Id must NOT be null.")
    private Long id;

    @ApiModelProperty(required = true, example = "burger without onion", value = "comments")
    @Size(max = 255, message = "Comments cannot be longer than 255 characters.")
    @NotNull(message = "Comments must NOT be null.")
    private String comments;

    @ApiModelProperty(required = true, example = "3", value = "tableId")
    @NotNull(message = "Table id must NOT be null.")
    private Long tableId;

    @ApiModelProperty(required = true, example = "2007-12-03T10:15:30", value = "orderDate")
    @NotNull(message = "Order date must NOT be null.")
    private String orderDate;

    @ApiModelProperty(required = true, value = "displayMenuItemDTOS")
    @NotNull(message = "List of DisplayMenuItems must NOT be null.")
    private List<DisplayMenuItemDTO> displayMenuItems;
}
