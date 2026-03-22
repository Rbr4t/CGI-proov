package com.example.restaurant.service;

import com.example.restaurant.DataLoader;
import com.example.restaurant.model.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurant.repo.TableRepository;

@Service
public class RecommendationService {

    private final DataLoader dataLoader;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationService reservationService;

    RecommendationService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

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

    /**
     * Method, that gives score to the table, takes in count:
     * 1. vacant seats and
     * 2. features the table matches
     * 
     * Higher score -> better matching table for the party
     * 
     * @param table
     * @param partySize
     * @param features
     * @return score
     */
    public int calculateScore(Table table, int partySize, String features) {
        int score = 0;

        // score by vacant seats
        score -= (table.getCapacity() - partySize) * 10;

        // score by containing features
        if (features != null && !features.isEmpty()) {
            if (table.getFeatures().contains(features)) {
                score += 30;
            }
        }

        return score;
    }

    private double distance(Table a, Table b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public List<Table> getTablePairRecommendations(int partySize, LocalDateTime startTime) {
        List<Table> available = tableRepository.findAll().stream()
                .filter(t -> reservationService.isTableAvailable(t.getId(), startTime))
                .collect(Collectors.toList());

        List<Table> bestPair = new ArrayList<>();
        double bestScore = 10000;
        int maxDist = 250;

        for (int i = 0; i < available.size(); i++) {
            for (int j = i + 1; j < available.size(); j++) {
                Table a = available.get(i);
                Table b = available.get(j);

                if (a.getCapacity() + b.getCapacity() >= partySize && distance(a, b) <= maxDist) {
                    double score = distance(a, b) + (a.getCapacity() + b.getCapacity() - partySize) * 10;

                    if (score < bestScore) {
                        bestScore = score;
                        bestPair = List.of(a, b);
                    }
                }
            }
        }

        return bestPair;
    }

}
