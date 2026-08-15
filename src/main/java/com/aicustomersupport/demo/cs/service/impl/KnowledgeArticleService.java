package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.KnowledgeArticleDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.KnowledgeArticleRepository;
import com.aicustomersupport.demo.cs.service.interfac.IKnowledgeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KnowledgeArticleService implements IKnowledgeArticleService {

    @Autowired
    private KnowledgeArticleRepository knowledgeArticleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response createArticle(KnowledgeArticleDto articleDto) {
        Response response = new Response();
        try {
            if (articleDto.getTitle() == null || articleDto.getTitle().isBlank()) {
                response.setStatusCode(400);
                response.setMessage("Article title is required");
                return response;
            }

            if (articleDto.getContent() == null || articleDto.getContent().isBlank()) {
                response.setStatusCode(400);
                response.setMessage("Article content is required");
                return response;
            }

            if (articleDto.getCategoryId() == null) {
                response.setStatusCode(400);
                response.setMessage("Category ID is required");
                return response;
            }

            Optional<Category> categoryOptional = categoryRepository.findById(articleDto.getCategoryId());
            if (categoryOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Category not found with id: " + articleDto.getCategoryId());
                return response;
            }

            KnowledgeArticle article = KnowledgeArticle.builder()
                    .title(articleDto.getTitle())
                    .content(articleDto.getContent())
                    .category(categoryOptional.get())
                    .active(articleDto.isActive())
                    .build();

            KnowledgeArticle savedArticle = knowledgeArticleRepository.save(article);

            response.setStatusCode(200);
            response.setMessage("Knowledge article created successfully");
            response.setKnowledgeArticle(convertToDto(savedArticle));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while creating article: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getArticle(Long id) {
        Response response = new Response();
        try {
            Optional<KnowledgeArticle> articleOptional = knowledgeArticleRepository.findById(id);
            if (articleOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Knowledge article not found with id: " + id);
                return response;
            }

            response.setStatusCode(200);
            response.setMessage("Knowledge article retrieved successfully");
            response.setKnowledgeArticle(convertToDto(articleOptional.get()));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching article: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllArticles(Pageable pageable) {
        Response response = new Response();
        try {
            Page<KnowledgeArticle> articlePage = knowledgeArticleRepository.findAll(pageable);
            List<KnowledgeArticleDto> articleDtos = articlePage.getContent()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Knowledge articles retrieved successfully");
            response.setKnowledgeArticles(articleDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching articles: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getArticlesByCategory(Long categoryId, Pageable pageable) {
        Response response = new Response();
        try {
            if (!categoryRepository.existsById(categoryId)) {
                response.setStatusCode(404);
                response.setMessage("Category not found with id: " + categoryId);
                return response;
            }

            Page<KnowledgeArticle> articlePage = knowledgeArticleRepository.findByCategoryId(categoryId, pageable);
            List<KnowledgeArticleDto> articleDtos = articlePage.getContent()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Articles for category retrieved successfully");
            response.setKnowledgeArticles(articleDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching articles by category: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getActiveArticles(Pageable pageable) {
        Response response = new Response();
        try {
            Page<KnowledgeArticle> articlePage = knowledgeArticleRepository.findByActive(true, pageable);
            List<KnowledgeArticleDto> articleDtos = articlePage.getContent()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Active articles retrieved successfully");
            response.setKnowledgeArticles(articleDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching active articles: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateArticle(KnowledgeArticleDto articleDto, Long id) {
        Response response = new Response();
        try {
            Optional<KnowledgeArticle> articleOptional = knowledgeArticleRepository.findById(id);
            if (articleOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Knowledge article not found with id: " + id);
                return response;
            }

            KnowledgeArticle article = articleOptional.get();

            if (articleDto.getTitle() != null && !articleDto.getTitle().isBlank()) {
                article.setTitle(articleDto.getTitle());
            }

            if (articleDto.getContent() != null && !articleDto.getContent().isBlank()) {
                article.setContent(articleDto.getContent());
            }

            if (articleDto.getCategoryId() != null) {
                Optional<Category> categoryOptional = categoryRepository.findById(articleDto.getCategoryId());
                if (categoryOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("Category not found with id: " + articleDto.getCategoryId());
                    return response;
                }
                article.setCategory(categoryOptional.get());
            }

            article.setActive(articleDto.isActive());

            KnowledgeArticle updatedArticle = knowledgeArticleRepository.save(article);

            response.setStatusCode(200);
            response.setMessage("Knowledge article updated successfully");
            response.setKnowledgeArticle(convertToDto(updatedArticle));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating article: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteArticle(Long id) {
        Response response = new Response();
        try {
            if (!knowledgeArticleRepository.existsById(id)) {
                response.setStatusCode(404);
                response.setMessage("Knowledge article not found with id: " + id);
                return response;
            }

            knowledgeArticleRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("Knowledge article deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting article: " + e.getMessage());
        }
        return response;
    }

    private KnowledgeArticleDto convertToDto(KnowledgeArticle article) {
        KnowledgeArticleDto dto = new KnowledgeArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        if (article.getCategory() != null) {
            dto.setCategoryId(article.getCategory().getId());
        }
        dto.setActive(article.isActive());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());
        return dto;
    }
}