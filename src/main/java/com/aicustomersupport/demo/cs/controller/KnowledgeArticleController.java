package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.KnowledgeArticleDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.service.interfac.IKnowledgeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge-articles")
public class KnowledgeArticleController {

    @Autowired
    private IKnowledgeArticleService knowledgeArticleService;

    @PostMapping
    public Response createArticle(
            @RequestBody KnowledgeArticleDto articleDto) {

        return knowledgeArticleService.createArticle(articleDto);
    }

    @GetMapping("/{id}")
    public Response getArticle(
            @PathVariable Long id) {

        return knowledgeArticleService.getArticle(id);
    }

    @GetMapping
    public Response getAllArticles(
            Pageable pageable) {

        return knowledgeArticleService
                .getAllArticles(pageable);
    }

    @GetMapping("/category/{categoryId}")
    public Response getArticlesByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {

        return knowledgeArticleService
                .getArticlesByCategory(
                        categoryId,
                        pageable
                );
    }

    @GetMapping("/active")
    public Response getActiveArticles(
            Pageable pageable) {

        return knowledgeArticleService
                .getActiveArticles(pageable);
    }

    @PutMapping("/{id}")
    public Response updateArticle(
            @RequestBody KnowledgeArticleDto articleDto,
            @PathVariable Long id) {

        return knowledgeArticleService
                .updateArticle(
                        articleDto,
                        id
                );
    }

    @DeleteMapping("/{id}")
    public Response deleteArticle(
            @PathVariable Long id) {

        return knowledgeArticleService
                .deleteArticle(id);
    }
}