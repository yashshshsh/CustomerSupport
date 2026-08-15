package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeArticleRepository
        extends JpaRepository<KnowledgeArticle, Long> {

    Page<KnowledgeArticle> findByCategoryId(
            Long categoryId,
            Pageable pageable
    );

    Page<KnowledgeArticle> findByActive(
            boolean active,
            Pageable pageable
    );
}