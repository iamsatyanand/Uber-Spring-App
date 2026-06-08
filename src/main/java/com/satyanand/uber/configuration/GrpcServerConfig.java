package com.satyanand.uber.configuration;

import com.satyanand.uber.service.impl.RideServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

    @Value("${grpc.server.port:9090}")
    private int grpcServerPort;
    private Server server;

    private final RideServiceImpl rideService;

    @PostConstruct
    public void startGrpcServer() throws IOException {
        server = ServerBuilder
                .forPort(grpcServerPort)
                .addService(rideService)
                .build()
                .start();

        System.out.println("gRPC server started on port :"+ grpcServerPort);

        new Thread(() -> {
            try{
                if(server != null){
                    server.awaitTermination();
                }
            }catch (InterruptedException exception){
                Thread.currentThread().interrupt();
                System.out.println("gRPC server interrupted");
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            System.out.println("Shutting down gRPC server...");
            if(server != null){
                server.shutdown();
            }
        }));
    }
}
