package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaiterDTO implements Serializable {
    @ApiModelProperty(required = true, example = "2", value = "id")
    @NotNull(message = "Id must NOT be null.")
    private Long id;

    @ApiModelProperty(required = true, example = "Eusebiu", value = "name")
    @Size(max = 255, message = "Name cannot be longer than 255 characters.")
    @NotNull(message = "Name must NOT be null.")
    private String name;
}
