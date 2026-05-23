package com.satyanand.uber.controller;

import com.satyanand.uber.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;




}
