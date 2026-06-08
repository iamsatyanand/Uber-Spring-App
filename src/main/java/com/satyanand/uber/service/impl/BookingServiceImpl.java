package com.satyanand.uber.service.impl;

import com.satyanand.uber.client.GrpcClient;
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

import java.math.BigDecimal;
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
    private final GrpcClient grpcClient;


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

        // handle driver assignment if provided
        Driver driver = null;

        Booking.BookingStatus status = Booking.BookingStatus.PENDING;

        if(request.getDriverId() != null){
            driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + request.getDriverId()));

            // Check if driver is available
            if (!driver.getIsAvailable()) {
                throw new IllegalStateException("Driver with id " + request.getDriverId() + " is not available");
            }

            // Assign driver and mark as unavailable
            driver.setIsAvailable(false);
            driverRepository.save(driver);
            status = Booking.BookingStatus.CONFIRMED;

        }


        String pickupLat = request.getPickupLocationLatitude() != null
                ? request.getPickupLocationLatitude().toString()
                : null;

        String pickupLong = request.getPickupLocationLongitude() != null
                ? request.getPickupLocationLongitude().toString()
                : null;

        if (pickupLat == null || pickupLong == null) {
            throw new IllegalArgumentException("Pickup location latitude and longitude are required");
        }

        // // Set default fare if not provided
        BigDecimal fare = request.getFare();
        if (fare == null) {
            fare = BigDecimal.ZERO; // Default fare, can be calculated later
        }

        // // Create booking
        Booking newBooking = Booking.builder()
                .passenger(passenger)
                .driver(driver)
                .pickupLocationLatitude(pickupLat)
                .pickupLocationLongitude(pickupLong)
                .dropoffLocation(request.getDropoffLocation())
                .status(status)
                .fare(fare)
                .scheduledPickupTime(request.getScheduledPickupTime())
                .build();

        Booking savedBooking = bookingRepository.save(newBooking);

        // Find the nearby drivers and then trigger an RPC to uber socket service to notify them

        List<DriverLocationDTO> nearByDrivers = locationService.getNearbyDrivers(request.getPickupLocationLatitude(), request.getPickupLocationLongitude(), 10.0);

        grpcClient.notifyDriversForNewRide(pickupLat, pickupLong, Integer.parseInt(savedBooking.getId().toString()), nearByDrivers.stream().map(DriverLocationDTO::getDriverId).toList());

        return bookingMapper.toResponse(savedBooking);
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
