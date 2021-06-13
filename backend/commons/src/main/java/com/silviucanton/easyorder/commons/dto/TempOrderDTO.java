package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempOrderDTO {
    @ApiModelProperty(required = true, example = "3", value = "id")
    @NotNull(message = "Id must NOT be null.")
    private Long id;

    @ApiModelProperty(required = true, example = "andrei99", value = "nickname")
    @Size(max = 255, message = "Nickname cannot be longer than 255 characters.")
    @NotNull(message = "Nickname must NOT be null.")
    private String nickname;

    @ApiModelProperty(required = true, example = "[4, 2, 1]", value = "menuItemIds")
    @NotNull(message = "List of MenuItem id-s must NOT be null.")
    private List<Long> menuItemIds;

    @ApiModelProperty(required = true, example = "some id", value = "sessionId")
    @Size(max = 255, message = "Session id cannot be longer than 255 characters.")
    @NotNull(message = "Session id must NOT be null.")
    private String sessionId;
}
