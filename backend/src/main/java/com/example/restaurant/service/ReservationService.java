package com.example.restaurant.service;

import java.text.Collator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.model.Reservation;
import com.example.restaurant.repo.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByTable(Long tableId) {
        return reservationRepository.findByTableId(tableId);
    }

    public boolean isTableAvailable(Long tableId, LocalDateTime startTime) {
        List<Reservation> existing = reservationRepository.findByTableId(tableId);
        LocalDateTime newEnd = startTime.plusHours(2);

        // 2 intervalls: 1. for reservation and 2. for starttime + Hours
        // if itnervalls are [a, b] and [c, d], then we are checking if a < d && b > c
        for (Reservation reservation : existing) {
            LocalDateTime exsistingEnd = reservation.getStartTime().plusHours(2);
            if (startTime.isBefore(exsistingEnd) && newEnd.isAfter(reservation.getStartTime().minusMinutes(1))) {
                return false;
            }
        }

        return true;
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
