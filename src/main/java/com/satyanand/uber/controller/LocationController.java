package com.satyanand.uber.controller;

import com.satyanand.uber.dto.DriverLocationDTO;
import com.satyanand.uber.dto.NearbyDriversRequestDTO;
import com.satyanand.uber.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/driverLocation")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody DriverLocationDTO driverLocation){
        Boolean saved = locationService.saveDriverLocation(driverLocation.getDriverId(), driverLocation.getLatitude(), driverLocation.getLongitude());
        return ResponseEntity.ok(saved);
    }

//    @GetMapping("/nearbyDrivers")
    @PostMapping("/nearbyDrivers")
    public ResponseEntity<List<DriverLocationDTO>> getNearbyDrivers(@RequestBody NearbyDriversRequestDTO request) {
        List<DriverLocationDTO> nearbyDrivers = locationService.getNearbyDrivers(request.getLatitude(), request.getLongitude(), request.getRadius());
        return ResponseEntity.ok(nearbyDrivers);
    }
}
