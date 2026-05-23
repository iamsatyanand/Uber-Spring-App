package com.satyanand.uber.service.impl;

import com.satyanand.uber.dto.DriverRequest;
import com.satyanand.uber.dto.DriverResponse;
import com.satyanand.uber.mapper.DriverMapper;
import com.satyanand.uber.repository.DriverRepository;
import com.satyanand.uber.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverResponse> findById(Long id) {
        return driverRepository.findById(id)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> findAll() {
        return driverRepository.findAll().stream()
                .map(driverMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverResponse> findByEmail(String email) {
        return driverRepository.findByEmail(email)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> findAvailableDrivers() {
        return driverRepository.findByIsAvailableTrue()
                .stream()
                .map(driverMapper::toResponse)
                .toList();
    }

    @Override
    public DriverResponse create(DriverRequest request) {
        return null;
    }

    @Override
    public DriverResponse update(Long id, DriverRequest request) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new IllegalArgumentException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }
}
