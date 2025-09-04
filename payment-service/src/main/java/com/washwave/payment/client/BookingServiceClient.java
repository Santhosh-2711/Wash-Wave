package com.washwave.payment.client;

import com.washwave.payment.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "booking-service", url = "http://localhost:8089")
public interface BookingServiceClient {
    
    @GetMapping("/bookings/admin/booking/{bookingId}")
    BookingDTO getBookingById(@PathVariable Long bookingId);
    
    @PostMapping("/bookings/admin/update-status/{bookingId}")
    BookingDTO updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status);
}