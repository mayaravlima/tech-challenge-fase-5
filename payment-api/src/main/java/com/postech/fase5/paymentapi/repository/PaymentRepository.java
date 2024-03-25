package com.postech.fase5.paymentapi.repository;

import com.postech.fase5.paymentapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentsByCartId(String cartId);

    Optional<List<Payment>> findPaymentsByUserId(Long userId);
}
