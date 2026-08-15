package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.KnowledgeArticleDto;
import com.aicustomersupport.demo.cs.dto.Response;
import org.springframework.data.domain.Pageable;

public interface IKnowledgeArticleService {

    Response createArticle(KnowledgeArticleDto articleDto);

    Response getArticle(Long id);

    Response getAllArticles(Pageable pageable);

    Response getArticlesByCategory(
            Long categoryId,
            Pageable pageable
    );

    Response getActiveArticles(Pageable pageable);

    Response updateArticle(
            KnowledgeArticleDto articleDto,
            Long id
    );

    Response deleteArticle(Long id);
}