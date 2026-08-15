package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;

public interface IKnowledgeArticleService {

    Response createArticle(KnowledgeArticle article);

    Response getArticle(Long id);

    Response getAllArticles();

    Response getArticlesByCategory(Long categoryId);

    Response getActiveArticles();

    Response updateArticle(KnowledgeArticle article, Long id);

    Response deleteArticle(Long id);
}