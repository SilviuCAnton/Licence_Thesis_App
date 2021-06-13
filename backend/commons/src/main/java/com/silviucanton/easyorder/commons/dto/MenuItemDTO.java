package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {
    @ApiModelProperty(required = true, example = "4", value = "id")
    @NotNull(message = "Id must NOT be null.")
    private long id;

    @ApiModelProperty(required = true, example = "tiramisu", value = "name")
    @Size(max = 255, message = "Name cannot be longer than 255 characters.")
    @NotNull(message = "Name must NOT be null.")
    private String name;

    @ApiModelProperty(required = true, example = "italian", value = "description")
    @Size(max = 255, message = "Description cannot be longer than 255 characters.")
    @NotNull(message = "Description must NOT be null.")
    private String description;

    @ApiModelProperty(required = true, example = "69.0", value = "price")
    @Min(value = 0, message = "Price cannot be negative.")
    private float price;

    @ApiModelProperty(required = true, example = "./data/marinara.jpg", value = "photoPath")
    @NotNull(message = "Photo path must NOT be null.")
    private String photoPath;

    @ApiModelProperty(example = "null", value = "photo")
    private byte[] photo;

    @ApiModelProperty(required = true, example = "true", value = "available")
    private boolean available;

    @ApiModelProperty(required = true, example = "desert", value = "category")
    @Size(max = 255, message = "Category cannot be longer than 255 characters.")
    @NotNull(message = "Category must NOT be null.")
    private String category;
}
