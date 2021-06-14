package com.silviucanton.easyorder.commons.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO implements Serializable {
    @ApiModelProperty(required = true, example = "Popescu Mirel", value = "owner")
    @Size(max = 255, message = "Owner cannot be longer than 255 characters.")
    @NotNull(message = "Owner must NOT be null.")
    private String owner;

    @ApiModelProperty(required = true, example = "1237550983227400", value = "cardNumber")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 digits long.")
    @NotNull(message = "Card number must NOT be null.")
    private String cardNumber;

    @ApiModelProperty(required = true, example = "03/28", value = "expirationDate")
    @Size(min = 5, max = 5, message = "Expiration date must be exactly 5 characters long.")
    @NotNull(message = "Expiration date must NOT be null.")
    private String expirationDate;

    @ApiModelProperty(required = true, example = "777", value = "cvv")
    @Min(value = 100, message = "CVV cannot be less than 3 digits.")
    @Max(value = 999, message = "CVV cannot be more than 3 digits.")
    @NotNull(message = "CVV must NOT be null.")
    private int cvv;
}
