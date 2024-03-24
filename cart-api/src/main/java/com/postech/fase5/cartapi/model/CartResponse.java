package com.postech.fase5.cartapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long id;
    private String cartId;
    private Integer totalItems;
    private Double totalCost;
    private List<Product> products;
}
