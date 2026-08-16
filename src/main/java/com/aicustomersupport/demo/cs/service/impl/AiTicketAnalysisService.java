package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.AiTicketAnalysisDto;
import com.aicustomersupport.demo.cs.dto.ArticleRecommendationDto;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.KnowledgeArticleRepository;
import com.aicustomersupport.demo.cs.service.interfac.IAiRecommendationService;
import com.aicustomersupport.demo.cs.service.interfac.IAiTicketAnalysisService;
import com.aicustomersupport.demo.cs.serviceai.AiClassificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiTicketAnalysisService
        implements IAiTicketAnalysisService {

    @Autowired
    private AiClassificationService aiClassificationService;

    @Autowired
    private IAiRecommendationService aiRecommendationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KnowledgeArticleRepository knowledgeArticleRepository;


    @Override
    public AiTicketAnalysisDto analyzeTicket(
            String ticketText
    ) {

        // ========================================================
        // CATEGORY AI
        // ========================================================

        Map<String, Object> categoryResult =
                aiClassificationService.classifyTicket(
                        ticketText
                );

        String predictedCategory =
                (String) categoryResult.get("category");

        Double categoryConfidence = null;

        if (categoryResult.get("confidence") != null) {

            categoryConfidence =
                    ((Number)
                            categoryResult.get("confidence"))
                            .doubleValue();
        }


        // ========================================================
        // PRIORITY AI
        // ========================================================

        Map<String, Object> priorityResult =
                aiClassificationService.predictPriority(
                        ticketText
                );

        String predictedPriority =
                (String) priorityResult.get("priority");

        Double priorityConfidence = null;

        if (priorityResult.get("confidence") != null) {

            priorityConfidence =
                    ((Number)
                            priorityResult.get("confidence"))
                            .doubleValue();
        }


        // ========================================================
        // FIND PREDICTED CATEGORY FROM DATABASE
        // ========================================================

        Category predictedCategoryEntity = null;

        if (predictedCategory != null) {

            predictedCategoryEntity =
                    categoryRepository
                            .findByName(predictedCategory)
                            .orElse(null);
        }


        // ========================================================
        // KNOWLEDGE ARTICLE RECOMMENDATIONS
        // ========================================================

        List<ArticleRecommendationDto> recommendations =
                List.of();

        if (predictedCategoryEntity != null) {

            Page<KnowledgeArticle> articles =
                    knowledgeArticleRepository
                            .findByActiveAndCategoryId(
                                    true,
                                    predictedCategoryEntity.getId(),
                                    PageRequest.of(0, 100)
                            );

            if (!articles.isEmpty()) {

                recommendations =
                        aiRecommendationService
                                .recommendArticles(
                                        ticketText,
                                        articles.getContent()
                                );
            }
        }


        // ========================================================
        // BUILD AI ANALYSIS RESULT
        // ========================================================

        return AiTicketAnalysisDto.builder()
                .category(predictedCategory)
                .categoryConfidence(categoryConfidence)
                .priority(predictedPriority)
                .priorityConfidence(priorityConfidence)
                .recommendations(recommendations)
                .build();
    }
}