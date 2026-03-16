package com.example.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.repo.TableRepository;
import com.example.restaurant.service.RecommendationService;
import com.example.restaurant.service.ReservationService;;

@RestController
@RequestMapping("/api")
public class RestaurantController {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RecommendationService recommendationService;

    // all tables GET

    // all bookings GET

    // recommendations GET

    // new booking POST

}
