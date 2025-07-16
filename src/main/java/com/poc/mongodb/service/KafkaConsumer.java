package com.poc.mongodb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.mongodb.model.HealthPlans;
import com.poc.mongodb.repository.HealthPlansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private HealthPlansRepository healthPlan_repo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "health-plan-topic", groupId = "health-bene-group")
    public void consume(String message) {
        try {
            HealthPlans healthPlans = objectMapper.readValue(message, HealthPlans.class);
            healthPlan_repo.save(healthPlans);
            System.out.println("Saved to MongoDB: " + healthPlans);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
