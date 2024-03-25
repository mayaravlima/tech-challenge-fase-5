package com.postech.fase5.cartapi.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cartId;
    private Long userId;
    private Integer totalItems;
    private Double totalCost;
    private String products;

}
