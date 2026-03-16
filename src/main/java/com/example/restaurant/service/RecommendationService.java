package com.example.restaurant.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.repo.TableRepository;

@Service
public class RecommendationService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationService reservationService;

    public List<Table> getRecommendations(int partySize, String zone, String features, LocalDateTime startTime) {
        List<Table> allTables = tableRepository.findAll();

        return allTables.stream()
                .filter(t -> reservationService.isTableAvailable(t.getId(), startTime))
                .filter(t -> t.getCapacity() >= partySize)
                .filter(t -> zone == null || zone.isEmpty() || t.getZone().equals(zone))
                .map(t -> {
                    t.setScore(calculateScore(t, partySize, features));
                    return t;
                })
                .sorted((a, b) -> b.getScore() - a.getScore())
                .collect(Collectors.toList());

    }

    public int calculateScore(Table table, int partySize, String features) {
        return 1;
    }

}
