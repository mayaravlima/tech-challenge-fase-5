package com.postech.fase5.cartapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartRequest {

    @NotNull(message = "Product Id cannot be null")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than 0")
    @JsonProperty("quantity")
    private Integer quantity;
}