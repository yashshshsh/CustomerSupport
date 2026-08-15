package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.KnowledgeArticleDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
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

    @Override
    public Response createArticle(KnowledgeArticleDto articleDto) {

        Response response = new Response();

        try {

            if (articleDto.getTitle() == null ||
                    articleDto.getTitle().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Article title is required");

                return response;
            }

            if (articleDto.getContent() == null ||
                    articleDto.getContent().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Article content is required");

                return response;
            }

            KnowledgeArticle article =
                    new KnowledgeArticle();

            article.setTitle(articleDto.getTitle());
            article.setContent(articleDto.getContent());
            article.setActive(articleDto.isActive());

            KnowledgeArticle savedArticle =
                    knowledgeArticleRepository.save(article);

            response.setStatusCode(200);
            response.setMessage(
                    "Article created successfully"
            );
            response.setKnowledgeArticle(
                    convertToDto(savedArticle)
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating article: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getArticle(Long id) {

        Response response = new Response();

        try {

            Optional<KnowledgeArticle> articleOptional =
                    knowledgeArticleRepository.findById(id);

            if (articleOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Article not found");

                return response;
            }

            response.setStatusCode(200);
            response.setMessage(
                    "Article retrieved successfully"
            );
            response.setKnowledgeArticle(
                    convertToDto(articleOptional.get())
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting article: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllArticles(Pageable pageable) {

        Response response = new Response();

        try {

            Page<KnowledgeArticle> articlePage =
                    knowledgeArticleRepository.findAll(pageable);

            List<KnowledgeArticleDto> articleDtos =
                    articlePage.getContent()
                            .stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Articles retrieved successfully"
            );
            response.setKnowledgeArticles(articleDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting articles: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getArticlesByCategory(
            Long categoryId,
            Pageable pageable) {

        Response response = new Response();

        try {

            Page<KnowledgeArticle> articlePage =
                    knowledgeArticleRepository
                            .findByCategoryId(
                                    categoryId,
                                    pageable
                            );

            List<KnowledgeArticleDto> articleDtos =
                    articlePage.getContent()
                            .stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Category articles retrieved successfully"
            );
            response.setKnowledgeArticles(articleDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting category articles: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getActiveArticles(Pageable pageable) {

        Response response = new Response();

        try {

            Page<KnowledgeArticle> articlePage =
                    knowledgeArticleRepository
                            .findByActive(
                                    true,
                                    pageable
                            );

            List<KnowledgeArticleDto> articleDtos =
                    articlePage.getContent()
                            .stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Active articles retrieved successfully"
            );
            response.setKnowledgeArticles(articleDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting active articles: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateArticle(
            KnowledgeArticleDto articleDto,
            Long id) {

        Response response = new Response();

        try {

            Optional<KnowledgeArticle> articleOptional =
                    knowledgeArticleRepository.findById(id);

            if (articleOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Article not found");

                return response;
            }

            KnowledgeArticle existingArticle =
                    articleOptional.get();

            if (articleDto.getTitle() != null &&
                    !articleDto.getTitle().isBlank()) {

                existingArticle.setTitle(
                        articleDto.getTitle()
                );
            }

            if (articleDto.getContent() != null &&
                    !articleDto.getContent().isBlank()) {

                existingArticle.setContent(
                        articleDto.getContent()
                );
            }

            existingArticle.setActive(
                    articleDto.isActive()
            );

            KnowledgeArticle updatedArticle =
                    knowledgeArticleRepository.save(
                            existingArticle
                    );

            response.setStatusCode(200);
            response.setMessage(
                    "Article updated successfully"
            );
            response.setKnowledgeArticle(
                    convertToDto(updatedArticle)
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating article: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteArticle(Long id) {

        Response response = new Response();

        try {

            if (!knowledgeArticleRepository.existsById(id)) {

                response.setStatusCode(404);
                response.setMessage("Article not found");

                return response;
            }

            knowledgeArticleRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage(
                    "Article deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting article: "
                            + e.getMessage()
            );
        }

        return response;
    }

    private KnowledgeArticleDto convertToDto(
            KnowledgeArticle article) {

        return new KnowledgeArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCategory() != null
                        ? article.getCategory().getId()
                        : null,
                article.isActive(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}