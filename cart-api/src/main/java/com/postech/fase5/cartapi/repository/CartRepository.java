package com.postech.fase5.cartapi.repository;

import com.postech.fase5.cartapi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartId(String cartId);
}
