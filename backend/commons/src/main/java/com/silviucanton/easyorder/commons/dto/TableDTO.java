package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDTO {

    @ApiModelProperty(required = true, example = "2", value = "id")
    @Min(value = 0, message = "Table id cannot be negative.")
    private long id;
}
