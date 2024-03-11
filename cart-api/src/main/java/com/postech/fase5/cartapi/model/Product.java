package com.postech.fase5.cartapi.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Long id;
    private String name;
    private Integer quantity;
    private Double amount;
}
