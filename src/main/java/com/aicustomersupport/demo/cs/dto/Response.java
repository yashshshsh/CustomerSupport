package com.aicustomersupport.demo.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    private int statusCode;

    private String message;


    // ============================================================
    // USER RESPONSES
    // ============================================================

    private UserDto user;

    private List<UserDto> users;


    // ============================================================
    // TICKET RESPONSES
    // ============================================================

    private TicketDto ticket;

    private List<TicketDto> tickets;


    // ============================================================
    // CATEGORY RESPONSES
    // ============================================================

    private CategoryDto category;

    private List<CategoryDto> categories;


    // ============================================================
    // TICKET STATUS HISTORY RESPONSES
    // ============================================================

    private TicketStatusHistoryDto ticketStatusHistory;

    private List<TicketStatusHistoryDto> ticketStatusHistories;


    // ============================================================
    // TICKET MESSAGE RESPONSES
    // ============================================================

    private TicketMessageDto ticketMessage;

    private List<TicketMessageDto> ticketMessages;


    // ============================================================
    // TICKET FEEDBACK RESPONSES
    // ============================================================

    private TicketFeedbackDto ticketFeedback;

    private List<TicketFeedbackDto> ticketFeedbacks;


    // ============================================================
    // TICKET ATTACHMENT RESPONSES
    // ============================================================

    private TicketAttachmentDto ticketAttachment;

    private List<TicketAttachmentDto> ticketAttachments;


    // ============================================================
    // KNOWLEDGE ARTICLE RESPONSES
    // ============================================================

    private KnowledgeArticleDto knowledgeArticle;

    private List<KnowledgeArticleDto> knowledgeArticles;


    // ============================================================
    // AI CATEGORY PREDICTION
    // ============================================================

    private String aiCategory;

    private Double aiCategoryConfidence;


    // ============================================================
    // AI PRIORITY PREDICTION
    // ============================================================

    private String aiPriority;

    private Double aiPriorityConfidence;


    // ============================================================
    // AI KNOWLEDGE ARTICLE RECOMMENDATIONS
    // ============================================================

    private List<ArticleRecommendationDto>
            knowledgeArticleRecommendations;


    // ============================================================
    // AI DECISION
    // ============================================================

    private String suggestedAction;

    private String decisionReason;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public Response(
            int statusCode,
            String message
    ) {

        this.statusCode = statusCode;

        this.message = message;
    }
}