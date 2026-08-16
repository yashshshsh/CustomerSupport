package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRecommendationDto {

    private KnowledgeArticle article;

    private double score;
}