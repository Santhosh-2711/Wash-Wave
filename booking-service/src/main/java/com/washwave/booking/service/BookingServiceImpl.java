package com.washwave.booking.service;

import com.washwave.booking.client.UserServiceClient;
import com.washwave.booking.dto.BookingRequestDTO;
import com.washwave.booking.dto.UserDTO;
import com.washwave.booking.entity.Booking;
import com.washwave.booking.entity.BookingStatus;
import com.washwave.booking.entity.ServiceType;
import com.washwave.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public Booking createBooking(BookingRequestDTO request, Long userId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setLocation(request.getLocation());
        booking.setBookingDate(LocalDate.parse(request.getBookingDate()));
        booking.setBookingTime(LocalTime.parse(request.getBookingTime()));
        booking.setServiceType(ServiceType.valueOf(request.getServiceType()));
        booking.setCarDetails(request.getCarDetails());
        booking.setSpecialInstructions(request.getSpecialInstructions());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPrice(ServiceType.valueOf(request.getServiceType()).getPrice());
        
        return bookingRepository.save(booking);
    }

    @Override
    public List<Map<String, Object>> getMyBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream().map(this::mapBookingWithUserDetails).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getPendingBookings() {
        List<Booking> bookings = bookingRepository.findByStatusOrderByCreatedAtAsc(BookingStatus.PENDING);
        return bookings.stream().map(this::mapBookingForWasher).collect(Collectors.toList());
    }

    @Override
    public Booking acceptBooking(Long bookingId, Long washerId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }
        
        booking.setWasherId(washerId);
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public Integer getBookingCountByUserId(Long userId) {
        return bookingRepository.countByUserId(userId);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, String status) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.valueOf(status));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking submitRating(Long bookingId, Integer rating, String comment, Long userId) {
        Booking booking = getBookingById(bookingId);
        
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("You can only rate your own bookings");
        }
        
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed bookings");
        }
        
        booking.setRating(rating);
        booking.setComment(comment);
        return bookingRepository.save(booking);
    }
    
    
    

@Override
public List<Map<String, Object>> getMyWasherBookings(Long washerId) {
    List<Booking> bookings = bookingRepository.findUpcomingBookingsByWasherId(washerId);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}



@Override
public List<Map<String, Object>> getPastWasherBookings(Long washerId) {
    List<Booking> bookings = bookingRepository.findPastBookingsByWasherId(washerId);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}


@Override
public List<Map<String, Object>> getPastCustomerBookings(Long userId) {
    List<Booking> bookings = bookingRepository.findPastBookingsByUserId(userId);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}


    private Map<String, Object> mapBookingWithUserDetails(Booking booking) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", booking.getId());
        result.put("location", booking.getLocation());
        result.put("bookingDate", booking.getBookingDate());
        result.put("bookingTime", booking.getBookingTime());
        result.put("serviceType", booking.getServiceType());
        result.put("status", booking.getStatus());
        result.put("carDetails", booking.getCarDetails());
        result.put("price", booking.getPrice());
        result.put("rating", booking.getRating());
        result.put("comment", booking.getComment());
        return result;
    }

    private Map<String, Object> mapBookingForWasher(Booking booking) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", booking.getId());
        result.put("userId", booking.getUserId());
        result.put("location", booking.getLocation());
        result.put("bookingDate", booking.getBookingDate());
        result.put("bookingTime", booking.getBookingTime());
        result.put("serviceType", booking.getServiceType());
        result.put("carDetails", booking.getCarDetails());
        result.put("price", booking.getPrice());
        
        // Fetch user details
        try {
            UserDTO user = userServiceClient.getUserById(booking.getUserId());
            result.put("customerPhone", user.getPhoneNumber());
        } catch (Exception e) {
            result.put("customerPhone", "N/A");
        }
        
        return result;
    }

	@Override
	public List<Map<String, Object>> getMyWBookings(Long WasherId) {
		List<Booking> bookings = bookingRepository.findByWasherIdOrderByCreatedAtDesc(WasherId);
        return bookings.stream().map(this::mapBookingWithUserDetails).collect(Collectors.toList());
		
	}
	

@Override
public List<Map<String, Object>> getUpcomingCustomerBookings(Long userId) {
    List<Booking> bookings = bookingRepository.findUpcomingBookingsByUserId(userId);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}


@Override
public List<Map<String, Object>> getCustomerBookingsByStatus(Long userId, BookingStatus status) {
    List<Booking> bookings = bookingRepository.findBookingsByUserIdAndStatus(userId, status);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}


@Override
public List<Map<String, Object>> getWasherBookingsByStatus(Long washerId, BookingStatus status) {
    List<Booking> bookings = bookingRepository.findBookingsByWasherIdAndStatus(washerId, status);
    return bookings.stream()
        .map(this::mapBookingWithUserDetails)
        .collect(Collectors.toList());
}



}