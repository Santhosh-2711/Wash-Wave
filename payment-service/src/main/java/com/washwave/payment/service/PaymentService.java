package com.washwave.payment.service;

import com.washwave.payment.dto.PaymentOrderDTO;
import com.washwave.payment.dto.PaymentVerificationDTO;

public interface PaymentService {
    PaymentOrderDTO createPaymentOrder(Long bookingId, Long userId);
    boolean verifyPayment(PaymentVerificationDTO verification);
}