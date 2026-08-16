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
public class AiTicketAnalysisDto {

    private String category;

    private Double categoryConfidence;

    private String priority;

    private Double priorityConfidence;

    private List<ArticleRecommendationDto> recommendations;
}