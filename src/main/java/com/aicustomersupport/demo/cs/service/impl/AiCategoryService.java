package com.aicustomersupport.demo.cs.service.impl;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AiCategoryService {

    private final RestTemplate restTemplate;

    private static final String AI_CATEGORY_URL =
            "http://localhost:8000/predict";

    public AiCategoryService() {
        this.restTemplate = new RestTemplate();
    }

    public String predictCategory(String ticketText) {

        Map<String, String> requestBody = Map.of(
                "text", ticketText
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        AI_CATEGORY_URL,
                        HttpMethod.POST,
                        requestEntity,
                        Map.class
                );

        if (response.getBody() == null) {
            throw new RuntimeException(
                    "AI category service returned empty response"
            );
        }

        Object category =
                response.getBody().get("category");

        if (category == null) {
            throw new RuntimeException(
                    "AI category prediction is missing"
            );
        }

        return category.toString();
    }
}