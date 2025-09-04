package com.washwave.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.washwave.payment.client.BookingServiceClient;
import com.washwave.payment.dto.BookingDTO;
import com.washwave.payment.dto.PaymentOrderDTO;
import com.washwave.payment.dto.PaymentVerificationDTO;
import com.washwave.payment.entity.Payment;
import com.washwave.payment.entity.PaymentStatus;
import com.washwave.payment.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    public PaymentOrderDTO createPaymentOrder(Long bookingId, Long userId) {
        try {
            // Get booking details
            BookingDTO booking = bookingServiceClient.getBookingById(bookingId);
            
            if (!booking.getUserId().equals(userId)) {
                throw new RuntimeException("You can only pay for your own bookings");
            }

            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", booking.getPrice().intValue() * 100); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_" + bookingId);

            Order order = razorpayClient.orders.create(orderRequest);

            // Save payment record
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setUserId(userId);
            payment.setAmount(booking.getPrice());
            payment.setRazorpayOrderId(order.get("id"));
            payment.setStatus(PaymentStatus.CREATED);
            paymentRepository.save(payment);

            PaymentOrderDTO response = new PaymentOrderDTO();
            response.setOrderId(order.get("id"));
            response.setAmount(booking.getPrice());
            response.setCurrency("INR");
            response.setBookingId(bookingId);

            return response;

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment order: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyPayment(PaymentVerificationDTO verification) {
        try {
            // Verify signature
            String generatedSignature = generateSignature(
                verification.getRazorpayOrderId(),
                verification.getRazorpayPaymentId(),
                razorpayKeySecret
            );

            if (!generatedSignature.equals(verification.getRazorpaySignature())) {
                return false;
            }

            // Update payment status
            Payment payment = paymentRepository.findByRazorpayOrderId(verification.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setRazorpayPaymentId(verification.getRazorpayPaymentId());
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            // Update booking status to COMPLETED
            bookingServiceClient.updateBookingStatus(payment.getBookingId(), "COMPLETED");

            return true;

        } catch (Exception e) {
            // Update payment status to failed
            Payment payment = paymentRepository.findByRazorpayOrderId(verification.getRazorpayOrderId())
                .orElse(null);
            if (payment != null) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }
            return false;
        }
    }

    private String generateSignature(String orderId, String paymentId, String secret) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(payload.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
}