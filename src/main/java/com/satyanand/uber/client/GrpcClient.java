package com.satyanand.uber.client;

import com.example.Uber.RideAcceptanceRequest;
import com.example.Uber.RideNotificationRequest;
import com.example.Uber.RideNotificationResponse;
import com.example.Uber.RideNotificationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrpcClient {

    @Value("${grpc.client.port:9091}")
    private int grpcClientPort;

    @Value("${grpc.client.host:localhost}")
    private String grpcClientHost;

    private ManagedChannel channel;

    private RideNotificationServiceGrpc.RideNotificationServiceBlockingStub rideNotificationServiceStub;

    @PostConstruct
    public void init(){
        channel = ManagedChannelBuilder.forAddress(grpcClientHost, grpcClientPort)
                .usePlaintext()
                .build();
        rideNotificationServiceStub = RideNotificationServiceGrpc.newBlockingStub(channel);
    }

    public boolean notifyDriversForNewRide(String pickUpLocationLatitude, String pickUpLocationLongitude, Integer bookingId, List<Integer> driverIds){
        RideNotificationRequest request = RideNotificationRequest.newBuilder()
                .setPickUpLocationLatitude(pickUpLocationLatitude)
                .setPickUpLocationLongitude(pickUpLocationLongitude)
                .setBookingId(bookingId)
                .addAllDriverIds(driverIds)
                .build();

        RideNotificationResponse response = rideNotificationServiceStub.notifyDriversForNewRide(request); // make the rpc call
        return response.getSuccess();
    }


}
