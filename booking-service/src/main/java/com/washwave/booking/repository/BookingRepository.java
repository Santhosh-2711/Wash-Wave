package com.washwave.booking.repository;

import com.washwave.booking.entity.Booking;
import com.washwave.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Booking> findByStatusOrderByCreatedAtAsc(BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.userId = ?1")
    Integer countByUserId(Long userId);
    
    List<Booking> findByWasherIdOrderByCreatedAtDesc(Long washerId);
    
    

      @Query("SELECT b FROM Booking b WHERE b.washerId = :washerId " +
       "AND (b.bookingDate > CURRENT_DATE OR (b.bookingDate = CURRENT_DATE AND b.bookingTime >= CURRENT_TIME)) " +
       "ORDER BY b.bookingDate ASC, b.bookingTime ASC")
    List<Booking> findUpcomingBookingsByWasherId( Long washerId);
      

     @Query("SELECT b FROM Booking b WHERE b.washerId = :washerId " +
       "AND (b.bookingDate < CURRENT_DATE OR (b.bookingDate = CURRENT_DATE AND b.bookingTime < CURRENT_TIME)) " +
       "ORDER BY b.bookingDate DESC, b.bookingTime DESC")
     List<Booking> findPastBookingsByWasherId(Long washerId);


@Query("SELECT b FROM Booking b WHERE b.userId = :userId " +
       "AND (b.bookingDate < CURRENT_DATE OR (b.bookingDate = CURRENT_DATE AND b.bookingTime < CURRENT_TIME)) " +
       "ORDER BY b.bookingDate DESC, b.bookingTime DESC")
List<Booking> findPastBookingsByUserId( Long userId);


@Query("SELECT b FROM Booking b WHERE b.userId = :userId " +
       "AND (b.bookingDate > CURRENT_DATE OR (b.bookingDate = CURRENT_DATE AND b.bookingTime >= CURRENT_TIME)) " +
       "ORDER BY b.bookingDate ASC, b.bookingTime ASC")
List<Booking> findUpcomingBookingsByUserId( Long userId);


@Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status = :status " +
       "ORDER BY b.bookingDate ASC, b.bookingTime ASC")
List<Booking> findBookingsByUserIdAndStatus(Long userId, BookingStatus status);


@Query("SELECT b FROM Booking b WHERE b.washerId = :washerId AND b.status = :status " +
       "ORDER BY b.bookingDate ASC, b.bookingTime ASC")
List<Booking> findBookingsByWasherIdAndStatus( Long washerId, BookingStatus status);




}