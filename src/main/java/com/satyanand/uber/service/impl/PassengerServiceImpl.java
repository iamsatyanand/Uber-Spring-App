package com.satyanand.uber.service.impl;

import com.satyanand.uber.dto.PassengerRequest;
import com.satyanand.uber.dto.PassengerResponse;
import com.satyanand.uber.mapper.PassengerMapper;
import com.satyanand.uber.repository.PassengerRepository;

import com.satyanand.uber.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<PassengerResponse> findById(Long id) {
        return passengerRepository.findById(id)
                .map(passengerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponse> findAll() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PassengerResponse> findByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .map(passengerMapper::toResponse);
    }

    @Override
    public PassengerResponse create(PassengerRequest request) {
        return null;
    }

    @Override
    public PassengerResponse update(Long id, PassengerRequest request) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new IllegalArgumentException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
    }
}