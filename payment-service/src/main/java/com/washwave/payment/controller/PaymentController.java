package com.washwave.payment.controller;

import com.washwave.payment.config.JwtService;
import com.washwave.payment.dto.PaymentOrderDTO;
import com.washwave.payment.dto.PaymentVerificationDTO;
import com.washwave.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")

public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentOrderDTO> createPaymentOrder(@RequestBody PaymentOrderRequest request,
                                                            @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        PaymentOrderDTO paymentOrder = paymentService.createPaymentOrder(request.getBookingId(), userId);
        return ResponseEntity.ok(paymentOrder);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerificationDTO verification) {
        boolean isValid = paymentService.verifyPayment(verification);
        if (isValid) {
            return ResponseEntity.ok("Payment verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Payment verification failed");
        }
    }

    public static class PaymentOrderRequest {
        private Long bookingId;
        
        public Long getBookingId() {
            return bookingId;
        }
        
        public void setBookingId(Long bookingId) {
            this.bookingId = bookingId;
        }
    }
}