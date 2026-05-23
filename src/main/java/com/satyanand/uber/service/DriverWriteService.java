package com.satyanand.uber.service;

import com.satyanand.uber.dto.DriverRequest;
import com.satyanand.uber.dto.DriverResponse;

/**
 * Interface for Driver write operations
 * Following Interface Segregation Principle
 */
public interface DriverWriteService {
    DriverResponse create(DriverRequest request);
    DriverResponse update(Long id, DriverRequest request);
    void deleteById(Long id);
}
