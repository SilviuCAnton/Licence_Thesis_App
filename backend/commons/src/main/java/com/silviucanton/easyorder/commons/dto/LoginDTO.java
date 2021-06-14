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
public class LoginDTO implements Serializable {
    @ApiModelProperty(required = true, example = "manager", value = "username")
    @Size(max = 255, message = "Username cannot be longer than 255 characters.")
    @NotNull(message = "Username must NOT be null.")
    private String username;

    @ApiModelProperty(required = true, example = "manager", value = "password")
    @Size(max = 255, message = "Password cannot be longer than 255 characters.")
    @NotNull(message = "Password must NOT be null.")
    private String password;
}
