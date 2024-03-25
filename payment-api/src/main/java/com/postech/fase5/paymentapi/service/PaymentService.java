package com.postech.fase5.paymentapi.service;

import com.postech.fase5.paymentapi.enums.PaymentStatus;
import com.postech.fase5.paymentapi.exception.PaymentException;
import com.postech.fase5.paymentapi.model.CartResponse;
import com.postech.fase5.paymentapi.model.PaymentResponse;
import com.postech.fase5.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String CART_SERVICE_URL = "http://CART-API/cart";

    @Qualifier("webclient")
    private final WebClient.Builder webBuilder;

    private final PaymentRepository paymentRepository;

    public CartResponse getCartDetails(String cartId) {
        var cart = webBuilder.build()
                .get()
                .uri(CART_SERVICE_URL)
                .header("cartId", cartId)
                .retrieve()
                .bodyToMono(CartResponse.class)
                .block();

        var payment = paymentRepository.findPaymentsByCartId(cartId);

        if (payment.isEmpty()) {
            var newPayment = cart.toCreatePayment();
            paymentRepository.save(newPayment);
            cart.setStatus(PaymentStatus.PENDING);
        } else {
            cart.setStatus(payment.get().getStatus());
        }

        return cart;
    }

    public PaymentResponse updatePaymentStatus(String cartId, PaymentStatus status) {
        if (PaymentStatus.PENDING.equals(status)) {
            throw new PaymentException("Invalid status", HttpStatus.BAD_REQUEST.value());
        }

        var payment = paymentRepository.findPaymentsByCartId(cartId);

        if (payment.isEmpty()) {
            throw new PaymentException("Payment not found", HttpStatus.NOT_FOUND.value());
        }

        var paymentToUpdate = payment.get();
        paymentToUpdate.setStatus(status);
        paymentRepository.save(paymentToUpdate);

        return PaymentResponse.fomEntity(paymentToUpdate);
    }

    public List<PaymentResponse> getAllPaymentsByUserId(Long userId) {
        var payments = paymentRepository.findPaymentsByUserId(userId);
        if (payments.isEmpty()) {
            throw new PaymentException("Payments not found", HttpStatus.NOT_FOUND.value());
        }
        return PaymentResponse.fromEntities(payments.get());
    }

}
