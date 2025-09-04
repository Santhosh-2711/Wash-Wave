package com.washwave.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private String location;
    private String bookingDate;
    private String bookingTime;
    private String serviceType;
    private String carDetails;
    private String specialInstructions;
}