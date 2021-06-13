package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayMenuItemDTO {
    @ApiModelProperty(required = true, example = "4", value = "id")
    private long id;
    @ApiModelProperty(required = true, example = "tiramisu", value = "name")
    private String name;
    @ApiModelProperty(required = true, example = "italian", value = "description")
    private String description;
    @ApiModelProperty(required = true, example = "69.0", value = "price")
    private float price;
    @ApiModelProperty(example = "/data/image.format", value = "photoPath")
    private String photoPath;
    @ApiModelProperty(required = true, example = "true", value = "available")
    private boolean available;
}
