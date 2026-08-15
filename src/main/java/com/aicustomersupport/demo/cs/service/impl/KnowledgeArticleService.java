package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.repository.KnowledgeArticleRepository;
import com.aicustomersupport.demo.cs.service.interfac.IKnowledgeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KnowledgeArticleService implements IKnowledgeArticleService {

    @Autowired
    private KnowledgeArticleRepository knowledgeArticleRepository;

    @Override
    public Response createArticle(KnowledgeArticle article) {

        Response response = new Response();

        try {

            if (article.getTitle() == null ||
                    article.getTitle().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Article title is required");

                return response;
            }

            if (article.getContent() == null ||
                    article.getContent().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Article content is required");

                return response;
            }

            KnowledgeArticle savedArticle =
                    knowledgeArticleRepository.save(article);

            response.setStatusCode(200);
            response.setMessage("Article created successfully");
            response.setKnowledgeArticle(savedArticle);

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

                response.setStatusCode(400);
                response.setMessage("Article not found");

                return response;
            }

            response.setStatusCode(200);
            response.setKnowledgeArticle(
                    articleOptional.get()
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
    public Response getAllArticles() {

        Response response = new Response();

        try {

            List<KnowledgeArticle> articles =
                    knowledgeArticleRepository.findAll();

            response.setStatusCode(200);
            response.setMessage(
                    "Articles retrieved successfully"
            );
            response.setKnowledgeArticles(articles);

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
    public Response getArticlesByCategory(Long categoryId) {

        Response response = new Response();

        try {

            List<KnowledgeArticle> articles =
                    knowledgeArticleRepository
                            .findByCategoryId(categoryId);

            response.setStatusCode(200);
            response.setMessage(
                    "Category articles retrieved successfully"
            );
            response.setKnowledgeArticles(articles);

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
    public Response getActiveArticles() {

        Response response = new Response();

        try {

            List<KnowledgeArticle> articles =
                    knowledgeArticleRepository.findByActive(true);

            response.setStatusCode(200);
            response.setMessage(
                    "Active articles retrieved successfully"
            );
            response.setKnowledgeArticles(articles);

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
            KnowledgeArticle article,
            Long id) {

        Response response = new Response();

        try {

            Optional<KnowledgeArticle> articleOptional =
                    knowledgeArticleRepository.findById(id);

            if (articleOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Article not found");

                return response;
            }

            KnowledgeArticle existingArticle =
                    articleOptional.get();

            if (article.getTitle() != null) {
                existingArticle.setTitle(
                        article.getTitle()
                );
            }

            if (article.getContent() != null) {
                existingArticle.setContent(
                        article.getContent()
                );
            }

            if (article.getCategory() != null) {
                existingArticle.setCategory(
                        article.getCategory()
                );
            }

            existingArticle.setActive(article.isActive());

            KnowledgeArticle updatedArticle =
                    knowledgeArticleRepository.save(
                            existingArticle
                    );

            response.setStatusCode(200);
            response.setMessage(
                    "Article updated successfully"
            );
            response.setKnowledgeArticle(updatedArticle);

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

                response.setStatusCode(400);
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
}