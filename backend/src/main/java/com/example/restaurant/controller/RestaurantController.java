package com.example.restaurant.controller;

import com.example.restaurant.repo.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurant.model.Reservation;
import com.example.restaurant.model.Table;
import com.example.restaurant.repo.TableRepository;
import com.example.restaurant.service.RecommendationService;
import com.example.restaurant.service.ReservationService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestaurantController {
    private final ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RecommendationService recommendationService;

    RestaurantController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // all tables GET
    @GetMapping("/tables")
    public List<Table> getTables() {
        return tableRepository.findAll();
    }

    // all bookings GET
    @GetMapping("reservations")
    public List<Reservation> getReservations() {
        return reservationService.getAllReservations();
    }

    // recommendations GET
    @GetMapping("recommendations")
    public List<Table> getMethodName(
            @RequestParam int partySize,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String features,
            @RequestParam String startTime) {

        LocalDateTime time = LocalDateTime.parse(startTime);
        if (partySize > 0) {
            return recommendationService.getRecommendations(partySize, zone, features, time);
        }
        return null;
    }

    // new booking POST
    @PostMapping("reservations")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

}
