package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.AiDecisionDto;
import com.aicustomersupport.demo.cs.dto.AiTicketAnalysisDto;
import com.aicustomersupport.demo.cs.service.interfac.IAiDecisionService;
import com.aicustomersupport.demo.cs.service.interfac.IAiTicketAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiDecisionService
        implements IAiDecisionService {

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


        String category =
                analysis.getCategory();

        Double categoryConfidence =
                analysis.getCategoryConfidence();

        String priority =
                analysis.getPriority();

        Double priorityConfidence =
                analysis.getPriorityConfidence();


        // ========================================================
        // DECISION
        // ========================================================

        String suggestedAction;

        String reason;


        // ========================================================
        // RULE 1
        // HIGH PRIORITY
        // ========================================================

        if ("HIGH".equalsIgnoreCase(priority)) {

            suggestedAction =
                    "AGENT_REVIEW";

            reason =
                    "High-priority ticket requires agent review.";

        }


        // ========================================================
        // RULE 2
        // ARTICLE AVAILABLE
        // ========================================================

        else if (
                analysis.getRecommendations() != null
                        && !analysis.getRecommendations().isEmpty()
        ) {

            suggestedAction =
                    "SUGGEST_ARTICLE";

            reason =
                    "Relevant knowledge articles were found " +
                            "for this ticket.";

        }


        // ========================================================
        // RULE 3
        // NO STRONG ARTICLE MATCH
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
                        analysis.getRecommendations()
                )

                .build();
    }
}