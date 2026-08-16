package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.model.KnowledgeArticle;

import java.util.List;

public interface IAiRecommendationService {

    List<KnowledgeArticle> recommendArticles(
            String ticketText,
            List<KnowledgeArticle> articles
    );
}