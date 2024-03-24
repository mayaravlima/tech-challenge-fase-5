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

    @PostMapping
    public ResponseEntity<CartResponse> addProductsToCart(
            @RequestBody List<CartRequest> products,
            @RequestHeader("cartId") String cartId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.processAddRequest(cartId, products));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@RequestHeader("cartId") String cartId, @RequestBody CartRequest cartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(cartId, cartRequest));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestHeader("cartId") String cartId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(cartId));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(@RequestHeader("cartId") String cartId, @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.removeItemFromCart(cartId, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("cartId") String cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
