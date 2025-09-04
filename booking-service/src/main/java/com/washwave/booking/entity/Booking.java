package com.washwave.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "washer_id")
    private Long washerId;
    
    private String location;
    
    @Column(name = "booking_date")
    private LocalDate bookingDate;
    
    @Column(name = "booking_time")
    private LocalTime bookingTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @Column(name = "car_details")
    private String carDetails;
    
    @Column(name = "special_instructions")
    private String specialInstructions;
    
    private Double price;
    
    private Integer rating;
    
    private String comment;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}