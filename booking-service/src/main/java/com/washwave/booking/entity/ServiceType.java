package com.washwave.booking.entity;

public enum ServiceType {
    BASIC_WASH(500.0),
    PREMIUM_WASH(750.0),
    DELUXE_PACKAGE(1000.0);
    
    private final Double price;
    
    ServiceType(Double price) {
        this.price = price;
    }
    
    public Double getPrice() {
        return price;
    }
}