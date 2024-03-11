package com.postech.fase5.productsapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    private String name;
    private Integer quantity;
    private Double amount;
}
