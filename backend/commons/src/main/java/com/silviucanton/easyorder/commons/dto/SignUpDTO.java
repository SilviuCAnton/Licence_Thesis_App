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
public class SignUpDTO {
    @ApiModelProperty(required = true, example = "John Smith", value = "name")
    @Size(max = 255, message = "Name cannot be longer than 255 characters.")
    @NotNull(message = "Name must NOT be null.")
    private String name;

    @ApiModelProperty(required = true, example = "john", value = "username")
    @Size(max = 255, message = "Username cannot be longer than 255 characters.")
    @NotNull(message = "Username must NOT be null.")
    private String username;

    @ApiModelProperty(required = true, example = "myPassword", value = "password")
    @Size(max = 255, message = "Password cannot be longer than 255 characters.")
    @NotNull(message = "Password must NOT be null.")
    private String password;

    @ApiModelProperty(required = true, example = "[WAITER]", value = "authorities")
    @NotNull(message = "List of authorities must NOT be null.")
    private List<String> authorities;
}
