package com.satyanand.uber.service.impl;

import com.example.Uber.RideAcceptanceRequest;
import com.example.Uber.RideAcceptanceResponse;
import com.example.Uber.RideServiceGrpc;
import com.satyanand.uber.service.BookingService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideServiceImpl extends RideServiceGrpc.RideServiceImplBase {

    private final BookingService bookingService;

    @Override
    public void acceptRide(RideAcceptanceRequest request, StreamObserver<RideAcceptanceResponse> responseObserver) {
        // call the booking service to update the ride with the new driver id
        Boolean success = bookingService.acceptRide(Long.parseLong(String.valueOf(request.getBookingId())), request.getDriverId());

        RideAcceptanceResponse response = RideAcceptanceResponse.newBuilder()
                .setSuccess(success)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
