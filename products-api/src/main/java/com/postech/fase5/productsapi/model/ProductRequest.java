package com.postech.fase5.productsapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Integer quantity;

    @NotBlank(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
