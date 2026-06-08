package com.satyanand.uber.service.impl;

import com.satyanand.uber.dto.BookingRequest;
import com.satyanand.uber.dto.BookingResponse;
import com.satyanand.uber.dto.DriverLocationDTO;
import com.satyanand.uber.entity.Booking;
import com.satyanand.uber.entity.Driver;
import com.satyanand.uber.entity.Passenger;
import com.satyanand.uber.mapper.BookingMapper;
import com.satyanand.uber.repository.BookingRepository;
import com.satyanand.uber.repository.DriverRepository;
import com.satyanand.uber.repository.PassengerRepository;
import com.satyanand.uber.service.BookingService;
import com.satyanand.uber.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final LocationService locationService;


    @Override
    @Transactional(readOnly = true)
    public Optional<BookingResponse> findById(Long id) {
        return bookingRepository.findById(id).map(bookingMapper::toResponse);
    }

    @Override
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> findByPassengerId(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + passengerId));

        return bookingRepository.findByPassenger(passenger).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> findByDriverId(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(
                () -> new IllegalArgumentException("Driver not found with id: " + driverId));

        return bookingRepository.findByDriver(driver).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse create(BookingRequest request) {

        Passenger passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: "+request.getPassengerId()));

        String pickupLat = request.getPickupLocationLatitude() != null
                ? request.getPickupLocationLatitude().toString()
                : null;

        String pickupLong = request.getPickupLocationLongitude() != null
                ? request.getPickupLocationLongitude().toString()
                : null;

        Booking newBooking = Booking.builder()
                .passenger(passenger)
                .pickupLocationLatitude(pickupLat)
                .pickupLocationLongitude(pickupLong)
                .status(Booking.BookingStatus.PENDING)
                .build();

        List<DriverLocationDTO> nearByDrivers = locationService.getNearbyDrivers(request.getPickupLocationLatitude(), request.getPickupLocationLongitude(), 10.0);



        return null;
    }

    @Override
    public BookingResponse update(Long id, BookingRequest request) {
        return null;
    }

    @Override
    public BookingResponse updateStatus(Long id, Booking.BookingStatus status) {
        return null;
    }

    @Override
    public Boolean acceptRide(Long id, Integer driverId) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
