package com.satyanand.uber.service;

import com.satyanand.uber.dto.PassengerRequest;
import com.satyanand.uber.dto.PassengerResponse;

/**
 * Interface for Passenger write operations
 * Following Interface Segregation Principle
 */
public interface PassengerWriteService {
    PassengerResponse create(PassengerRequest request);
    PassengerResponse update(Long id, PassengerRequest request);
    void deleteById(Long id);
}
