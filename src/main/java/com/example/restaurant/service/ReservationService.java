package com.example.restaurant.service;

import java.time.LocalDateTime;
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

        for (Reservation reservation : existing) {
            LocalDateTime endTime = reservation.getStartTime().plusHours(2);
            if (startTime.isBefore(endTime) && startTime.isAfter(reservation.getStartTime().minusMinutes(1))) {

                return false;
            }
        }

        return true;
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
