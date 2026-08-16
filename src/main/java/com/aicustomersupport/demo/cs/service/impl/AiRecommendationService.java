package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.ArticleRecommendationDto;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.service.interfac.IAiRecommendationService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiRecommendationService implements IAiRecommendationService {

    private final RestTemplate restTemplate;

    private static final String AI_SERVICE_URL =
            "http://localhost:8000/recommend-articles";

    public AiRecommendationService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<ArticleRecommendationDto> recommendArticles(
            String ticketText,
            List<KnowledgeArticle> articles
    ) {

        if (articles == null || articles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> articleRequests =
                new ArrayList<>();

        for (KnowledgeArticle article : articles) {

            Map<String, Object> articleRequest = Map.of(
                    "id", article.getId(),
                    "title", article.getTitle(),
                    "content", article.getContent()
            );

            articleRequests.add(articleRequest);
        }

        Map<String, Object> requestBody = Map.of(
                "text", ticketText,
                "articles", articleRequests,
                "top_k", 3
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<Map<String, Object>> requestEntity =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        AI_SERVICE_URL,
                        HttpMethod.POST,
                        requestEntity,
                        Map.class
                );

        if (response.getBody() == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> recommendations =
                (List<Map<String, Object>>)
                        response.getBody()
                                .get("recommendations");

        List<ArticleRecommendationDto> result =
                new ArrayList<>();

        if (recommendations == null) {
            return result;
        }

        for (Map<String, Object> recommendation
                : recommendations) {

            Number articleId =
                    (Number) recommendation.get(
                            "articleId"
                    );

            Number score =
                    (Number) recommendation.get(
                            "score"
                    );

            if (articleId == null || score == null) {
                continue;
            }

            for (KnowledgeArticle article : articles) {

                if (article.getId().equals(
                        articleId.longValue()
                )) {

                    result.add(
                            new ArticleRecommendationDto(
                                    article,
                                    score.doubleValue()
                            )
                    );

                    break;
                }
            }
        }

        return result;
    }
}