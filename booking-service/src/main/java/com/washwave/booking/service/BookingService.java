package com.washwave.booking.service;

import com.washwave.booking.dto.BookingRequestDTO;
import com.washwave.booking.entity.Booking;
import com.washwave.booking.entity.BookingStatus;

import java.util.List;
import java.util.Map;

public interface BookingService {
    Booking createBooking(BookingRequestDTO request, Long userId);
    List<Map<String, Object>> getMyBookings(Long userId);
    List<Map<String, Object>> getPendingBookings();
    Booking acceptBooking(Long bookingId, Long washerId);
    Booking getBookingById(Long bookingId);
    Integer getBookingCountByUserId(Long userId);
    Booking updateBookingStatus(Long bookingId, String status);
    Booking submitRating(Long bookingId, Integer rating, String comment, Long userId);
    List<Map<String, Object>> getMyWasherBookings(Long washerId);
    List<Map<String, Object>> getPastWasherBookings(Long washerId);
    List<Map<String, Object>> getPastCustomerBookings(Long userId);
    List<Map<String, Object>> getMyWBookings(Long WasherId);
    List<Map<String, Object>> getUpcomingCustomerBookings(Long userId);
    List<Map<String, Object>> getCustomerBookingsByStatus(Long userId, BookingStatus status);
    List<Map<String, Object>> getWasherBookingsByStatus(Long washerId, BookingStatus status);
}