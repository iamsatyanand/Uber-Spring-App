package com.satyanand.uber.service;

import com.satyanand.uber.dto.DriverLocationDTO;

import java.util.List;

public interface LocationService {

    Boolean saveDriverLocation(Integer driverId, Double latitude, Double longitude);

    List<DriverLocationDTO> getNearbyDrivers(Double latitude, Double longitude, Double radius);
}
