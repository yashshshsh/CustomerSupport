package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.AiDecisionDto;
import com.aicustomersupport.demo.cs.dto.AiTicketAnalysisDto;
import com.aicustomersupport.demo.cs.dto.ArticleRecommendationDto;
import com.aicustomersupport.demo.cs.service.interfac.IAiDecisionService;
import com.aicustomersupport.demo.cs.service.interfac.IAiTicketAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiDecisionService
        implements IAiDecisionService {

    // ============================================================
    // ARTICLE MATCH THRESHOLD
    // ============================================================

    private static final double STRONG_ARTICLE_SCORE = 0.70;


    @Autowired
    private IAiTicketAnalysisService aiTicketAnalysisService;


    @Override
    public AiDecisionDto makeDecision(
            String ticketText
    ) {

        // ========================================================
        // RUN EXISTING AI ANALYSIS
        // ========================================================

        AiTicketAnalysisDto analysis =
                aiTicketAnalysisService.analyzeTicket(
                        ticketText
                );


        // ========================================================
        // GET AI RESULTS
        // ========================================================

        String category =
                analysis.getCategory();

        Double categoryConfidence =
                analysis.getCategoryConfidence();

        String priority =
                analysis.getPriority();

        Double priorityConfidence =
                analysis.getPriorityConfidence();


        List<ArticleRecommendationDto> recommendations =
                analysis.getRecommendations();


        // ========================================================
        // FIND BEST ARTICLE SCORE
        // ========================================================

        double bestArticleScore = 0.0;

        if (recommendations != null
                && !recommendations.isEmpty()) {

            for (ArticleRecommendationDto recommendation
                    : recommendations) {

                if (recommendation != null) {

                    bestArticleScore =
                            Math.max(
                                    bestArticleScore,
                                    recommendation.getScore()
                            );
                }
            }
        }


        // ========================================================
        // DECISION VARIABLES
        // ========================================================

        String suggestedAction;

        String reason;


        // ========================================================
        // RULE 1
        // HIGH PRIORITY ALWAYS REQUIRES AGENT REVIEW
        // ========================================================

        if ("HIGH".equalsIgnoreCase(priority)) {

            suggestedAction =
                    "AGENT_REVIEW";

            reason =
                    "High-priority ticket requires agent review.";
        }


        // ========================================================
        // RULE 2
        // LOW/MEDIUM + STRONG ARTICLE MATCH
        // ========================================================

        else if (
                bestArticleScore >= STRONG_ARTICLE_SCORE
        ) {

            suggestedAction =
                    "SUGGEST_ARTICLE";

            reason =
                    "A strong knowledge article match was found "
                            + "(score: "
                            + String.format(
                            "%.4f",
                            bestArticleScore
                    )
                            + ").";
        }


        // ========================================================
        // RULE 3
        // LOW/MEDIUM + WEAK ARTICLE MATCH
        // ========================================================

        else {

            suggestedAction =
                    "AGENT_REVIEW";

            reason =
                    "No strong knowledge article match was found.";
        }


        // ========================================================
        // BUILD DECISION RESULT
        // ========================================================

        return AiDecisionDto.builder()

                .aiCategory(category)

                .aiCategoryConfidence(
                        categoryConfidence
                )

                .aiPriority(priority)

                .aiPriorityConfidence(
                        priorityConfidence
                )

                .suggestedAction(
                        suggestedAction
                )

                .reason(reason)

                .knowledgeArticleRecommendations(
                        recommendations
                )

                .build();
    }
}