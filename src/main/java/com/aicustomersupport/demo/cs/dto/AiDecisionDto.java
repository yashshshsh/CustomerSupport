package com.aicustomersupport.demo.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiDecisionDto {

    private String aiCategory;

    private Double aiCategoryConfidence;

    private String aiPriority;

    private Double aiPriorityConfidence;

    private String suggestedAction;

    private String reason;

    private List<ArticleRecommendationDto> knowledgeArticleRecommendations;
}