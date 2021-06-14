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
public class MenuSectionDTO implements Serializable {
    @ApiModelProperty(required = true, example = "desert", value = "categoryName")
    @Size(max = 255, message = "Category name cannot be longer than 255 characters.")
    @NotNull(message = "Category name must NOT be null.")
    private String categoryName;

    @ApiModelProperty(required = true, value = "menuItems")
    @NotNull(message = "List of menu items must NOT be null.")
    private List<DisplayMenuItemDTO> menuItems;
}
