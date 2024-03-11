package com.postech.fase5.cartapi.controller;

import com.postech.fase5.cartapi.model.CartRequest;
import com.postech.fase5.cartapi.model.CartResponse;
import com.postech.fase5.cartapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private CartService cartService;

    @PostMapping("{userId}/products")
    public ResponseEntity<CartResponse> addProductsToCart(@PathVariable Long userId, @RequestBody List<CartRequest> products) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.processAddRequest(userId, products));
    }

    @PostMapping("{userId}/products/{productId}")
    public ResponseEntity<CartResponse> addProductToCart(@PathVariable Long userId, @RequestBody CartRequest cartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(userId, cartRequest));
    }

    @GetMapping("{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(userId));
    }

    @DeleteMapping("{userId}/products/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.removeItemFromCart(userId, productId));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
