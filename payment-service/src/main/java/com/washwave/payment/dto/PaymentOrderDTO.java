package com.washwave.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderDTO {
    private String orderId;
    private Double amount;
    private String currency;
    private Long bookingId;
}