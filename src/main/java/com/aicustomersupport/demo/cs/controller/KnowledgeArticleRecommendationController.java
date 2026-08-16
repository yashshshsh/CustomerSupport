package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.ArticleRecommendationDto;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.KnowledgeArticleRepository;
import com.aicustomersupport.demo.cs.service.impl.AiCategoryService;
import com.aicustomersupport.demo.cs.service.interfac.IAiRecommendationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge-articles")
public class KnowledgeArticleRecommendationController {

    private final KnowledgeArticleRepository knowledgeArticleRepository;

    private final CategoryRepository categoryRepository;

    private final IAiRecommendationService aiRecommendationService;

    private final AiCategoryService aiCategoryService;

    public KnowledgeArticleRecommendationController(
            KnowledgeArticleRepository knowledgeArticleRepository,
            CategoryRepository categoryRepository,
            IAiRecommendationService aiRecommendationService,
            AiCategoryService aiCategoryService
    ) {
        this.knowledgeArticleRepository =
                knowledgeArticleRepository;

        this.categoryRepository =
                categoryRepository;

        this.aiRecommendationService =
                aiRecommendationService;

        this.aiCategoryService =
                aiCategoryService;
    }

    @PostMapping("/recommend")
    public List<ArticleRecommendationDto> recommendArticles(
            @RequestBody RecommendationRequest request
    ) {

        // 1. Predict ticket category using AI
        String predictedCategory =
                aiCategoryService.predictCategory(
                        request.text()
                );

        // 2. Find the corresponding category
        //    from the database
        Category category =
                categoryRepository
                        .findByName(
                                predictedCategory.toUpperCase()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found: "
                                                + predictedCategory
                                )
                        );

        // 3. Get only active articles
        //    belonging to predicted category
        Page<KnowledgeArticle> activeArticles =
                knowledgeArticleRepository
                        .findByActiveAndCategoryId(
                                true,
                                category.getId(),
                                PageRequest.of(0, 100)
                        );

        // 4. Rank filtered articles using TF-IDF
        return aiRecommendationService
                .recommendArticles(
                        request.text(),
                        activeArticles.getContent()
                );
    }

    public record RecommendationRequest(
            String text
    ) {
    }
}