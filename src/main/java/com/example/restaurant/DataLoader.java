package com.example.restaurant;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.restaurant.model.Reservation;
import com.example.restaurant.model.Table;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Random rand = new Random();

        // chatgpt generated me some dummy data
        String[][] tables = {
                { "2", "indoor", "window", "100", "100" },
                { "2", "indoor", "quiet", "100", "220" },
                { "4", "indoor", "window", "260", "100" },
                { "4", "indoor", "", "260", "220" },
                { "4", "indoor", "quiet", "260", "340" },
                { "6", "indoor", "", "420", "100" },
                { "6", "indoor", "accessible", "420", "280" },
                { "8", "indoor", "", "580", "100" },
                { "2", "terrace", "window", "100", "500" },
                { "2", "terrace", "", "100", "620" },
                { "4", "terrace", "window", "260", "500" },
                { "4", "terrace", "", "260", "620" },
                { "6", "terrace", "", "420", "500" },
                { "8", "private", "quiet", "580", "400" },
                { "10", "private", "", "580", "550" },
        };

        for (String[] t : tables) {
            Table table = new Table();
            table.setCapacity(Integer.parseInt(t[0]));
            table.setZone(t[1]);
            table.setFeatures(t[2]);
            table.setX(Integer.parseInt(t[3]));
            table.setY(Integer.parseInt(t[4]));
            em.persist(table);
        }

        em.flush();

        for (int i = 0; i < tables.length; i++) {
            if (rand.nextBoolean()) {
                Reservation r = new Reservation();
                r.setTableId((long) i);
                r.setPartySize(rand.nextInt(4) + 1);
                r.setStartTime(LocalDateTime.now().plusHours(rand.nextInt(5)));
                r.setCustomerName("Customer " + i);
                em.persist(r);
            }
        }
    }

}
