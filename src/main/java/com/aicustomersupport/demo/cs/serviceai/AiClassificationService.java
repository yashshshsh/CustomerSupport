package com.aicustomersupport.demo.cs.serviceai;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AiClassificationService {

    private final RestClient restClient;

    public AiClassificationService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }

    // ============================================================
    // CATEGORY PREDICTION
    // ============================================================

    public Map<String, Object> classifyTicket(String text) {

        return restClient.post()
                .uri("/predict")
                .body(Map.of("text", text))
                .retrieve()
                .body(Map.class);
    }


    // ============================================================
    // PRIORITY PREDICTION
    // ============================================================

    public Map<String, Object> predictPriority(String text) {

        return restClient.post()
                .uri("/predict-priority")
                .body(Map.of("text", text))
                .retrieve()
                .body(Map.class);
    }
}