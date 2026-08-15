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


    // CREATE
    @PostMapping
    public Response createArticle(
            @RequestBody KnowledgeArticleDto articleDto) {

        return knowledgeArticleService.createArticle(
                articleDto
        );
    }


    // GET BY ID
    @GetMapping("/{id}")
    public Response getArticle(
            @PathVariable Long id) {

        return knowledgeArticleService.getArticle(id);
    }


    // GET ALL
    @GetMapping
    public Response getAllArticles(
            Pageable pageable) {

        return knowledgeArticleService.getAllArticles(
                pageable
        );
    }


    // GET BY CATEGORY
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


    // GET ACTIVE
    @GetMapping("/active")
    public Response getActiveArticles(
            Pageable pageable) {

        return knowledgeArticleService
                .getActiveArticles(
                        pageable
                );
    }


    // UPDATE
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


    // DELETE
    @DeleteMapping("/{id}")
    public Response deleteArticle(
            @PathVariable Long id) {

        return knowledgeArticleService.deleteArticle(id);
    }
}