package com.satyanand.uber.service;

import com.satyanand.uber.dto.BookingRequest;
import com.satyanand.uber.dto.BookingResponse;
import com.satyanand.uber.entity.Booking;

/**
 * Interface for Booking write operations
 * Following Interface Segregation Principle
 */
public interface BookingWriteService {
    BookingResponse create(BookingRequest request);
    BookingResponse update(Long id, BookingRequest request);
    BookingResponse updateStatus(Long id, Booking.BookingStatus status);
    Boolean acceptRide(Long id, Integer driverId);
    void deleteById(Long id);
}
