package com.postech.fase5.paymentapi.controller;

import com.postech.fase5.paymentapi.enums.PaymentStatus;
import com.postech.fase5.paymentapi.model.CartResponse;
import com.postech.fase5.paymentapi.model.PaymentResponse;
import com.postech.fase5.paymentapi.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCartDetails(@RequestHeader("cartId") String cartId) {
        return ResponseEntity.ok().body(paymentService.getCartDetails(cartId));
    }

    @PutMapping("/update")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @RequestHeader("cartId") String cartId,
            @RequestParam("status") PaymentStatus status) {
        return ResponseEntity.ok().body(paymentService.updatePaymentStatus(cartId, status));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPayments(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok().body(paymentService.getAllPaymentsByUserId(userId));
    }
}
