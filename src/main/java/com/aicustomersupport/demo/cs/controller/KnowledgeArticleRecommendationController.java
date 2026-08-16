package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.ArticleRecommendationDto;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.repository.KnowledgeArticleRepository;
import com.aicustomersupport.demo.cs.service.interfac.IAiRecommendationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge-articles")
public class KnowledgeArticleRecommendationController {

    private final KnowledgeArticleRepository knowledgeArticleRepository;
    private final IAiRecommendationService aiRecommendationService;

    public KnowledgeArticleRecommendationController(
            KnowledgeArticleRepository knowledgeArticleRepository,
            IAiRecommendationService aiRecommendationService
    ) {
        this.knowledgeArticleRepository = knowledgeArticleRepository;
        this.aiRecommendationService = aiRecommendationService;
    }

    @PostMapping("/recommend")
    public List<ArticleRecommendationDto> recommendArticles(
            @RequestBody RecommendationRequest request
    ) {

        Page<KnowledgeArticle> activeArticles =
                knowledgeArticleRepository.findByActive(
                        true,
                        PageRequest.of(0, 100)
                );

        return aiRecommendationService.recommendArticles(
                request.text(),
                activeArticles.getContent()
        );
    }

    public record RecommendationRequest(
            String text
    ) {
    }
}