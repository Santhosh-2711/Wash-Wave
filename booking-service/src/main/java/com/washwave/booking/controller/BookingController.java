package com.washwave.booking.controller;

import com.washwave.booking.config.JwtService;
import com.washwave.booking.dto.BookingRequestDTO;
import com.washwave.booking.entity.Booking;
import com.washwave.booking.entity.BookingStatus;
import com.washwave.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")

public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtService jwtService;

   

    // Customer endpoints
    @PostMapping("/customer/create")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO request,
                                               @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        Booking booking = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer/getAll")
    public ResponseEntity<List<Map<String, Object>>> getMyBookings(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getMyBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/customer/count")
    public ResponseEntity<Integer> getBookingCount(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        Integer count = bookingService.getBookingCountByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/customer/review/{bookingId}")
    public ResponseEntity<Booking> submitRating(@PathVariable Long bookingId,
                                              @RequestParam Integer rating,
                                              @RequestParam String comment,
                                              @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        Booking booking = bookingService.submitRating(bookingId, rating, comment, userId);
        return ResponseEntity.ok(booking);
    }

    // Washer endpoints
    @GetMapping("/washer/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingBookings() {
        List<Map<String, Object>> bookings = bookingService.getPendingBookings();
        return ResponseEntity.ok(bookings);
    }
  
    @PostMapping("/washer/accept/{bookingId}")
    public ResponseEntity<Booking> acceptBooking(@PathVariable Long bookingId,
                                               @RequestHeader("Authorization") String token) {
        Long washerId = jwtService.extractUserId(token.substring(7));
        Booking booking = bookingService.acceptBooking(bookingId, washerId);
        return ResponseEntity.ok(booking);
    }

    // Admin endpoints
    @GetMapping("/admin/booking/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/update-status/{bookingId}")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable Long bookingId,
                                                     @RequestParam String status) {
        Booking booking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(booking);
    }
    
 // 🔹 Upcoming bookings for customer
    @GetMapping("/customer/upcoming")
    public ResponseEntity<List<Map<String, Object>>> getUpcomingCustomerBookings(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getUpcomingCustomerBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    // 🔹 Past bookings for customer
    @GetMapping("/customer/past")
    public ResponseEntity<List<Map<String, Object>>> getPastCustomerBookings(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getPastCustomerBookings(userId);
        return ResponseEntity.ok(bookings);
    }
    
    // 🔹 All bookings for washer (sorted by createdAt)
    @GetMapping("/washer/getAll")
    public ResponseEntity<List<Map<String, Object>>> getAllWasherBookings(@RequestHeader("Authorization") String token) {
        Long washerId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getMyWBookings(washerId);
        return ResponseEntity.ok(bookings);
    }

    // 🔹 Upcoming bookings for washer
    @GetMapping("/washer/upcoming")
    public ResponseEntity<List<Map<String, Object>>> getUpcomingWasherBookings(@RequestHeader("Authorization") String token) {
        Long washerId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getMyWasherBookings(washerId);
        return ResponseEntity.ok(bookings);
    }

    // 🔹 Past bookings for washer
    @GetMapping("/washer/past")
    public ResponseEntity<List<Map<String, Object>>> getPastWasherBookings(@RequestHeader("Authorization") String token) {
        Long washerId = jwtService.extractUserId(token.substring(7));
        List<Map<String, Object>> bookings = bookingService.getPastWasherBookings(washerId);
        return ResponseEntity.ok(bookings);
    }
    

@GetMapping("/customer/status")
public ResponseEntity<List<Map<String, Object>>> getCustomerBookingsByStatus(
        @RequestHeader("Authorization") String token,
        @RequestParam BookingStatus status) {
    Long userId = jwtService.extractUserId(token.substring(7));
    List<Map<String, Object>> bookings = bookingService.getCustomerBookingsByStatus(userId, status);
    return ResponseEntity.ok(bookings);
}


@GetMapping("/washer/status")
public ResponseEntity<List<Map<String, Object>>> getWasherBookingsByStatus(
        @RequestHeader("Authorization") String token,
        @RequestParam BookingStatus status) {
    Long washerId = jwtService.extractUserId(token.substring(7));
    List<Map<String, Object>> bookings = bookingService.getWasherBookingsByStatus(washerId, status);
    return ResponseEntity.ok(bookings);
}



}