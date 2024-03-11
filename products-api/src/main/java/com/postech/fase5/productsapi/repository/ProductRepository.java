package com.postech.fase5.productsapi.repository;

import com.postech.fase5.productsapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
