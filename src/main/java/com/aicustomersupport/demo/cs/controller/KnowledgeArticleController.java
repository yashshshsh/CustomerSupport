package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.service.interfac.IKnowledgeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge-articles")
public class KnowledgeArticleController {

    @Autowired
    private IKnowledgeArticleService knowledgeArticleService;

    @PostMapping
    public Response createArticle(
            @RequestBody KnowledgeArticle article) {

        return knowledgeArticleService.createArticle(article);
    }

    @GetMapping("/{id}")
    public Response getArticle(@PathVariable Long id) {

        return knowledgeArticleService.getArticle(id);
    }

    @GetMapping
    public Response getAllArticles() {

        return knowledgeArticleService.getAllArticles();
    }

    @GetMapping("/category/{categoryId}")
    public Response getArticlesByCategory(
            @PathVariable Long categoryId) {

        return knowledgeArticleService
                .getArticlesByCategory(categoryId);
    }

    @GetMapping("/active")
    public Response getActiveArticles() {

        return knowledgeArticleService.getActiveArticles();
    }

    @PutMapping("/{id}")
    public Response updateArticle(
            @RequestBody KnowledgeArticle article,
            @PathVariable Long id) {

        return knowledgeArticleService
                .updateArticle(article, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteArticle(@PathVariable Long id) {

        return knowledgeArticleService.deleteArticle(id);
    }
}