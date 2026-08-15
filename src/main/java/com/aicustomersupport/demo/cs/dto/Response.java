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

    // User responses
    private UserDto user;
    private List<UserDto> users;

    // Ticket responses
    private TicketDto ticket;
    private List<TicketDto> tickets;

    // Category responses (Updated to DTO)
    private CategoryDto category;
    private List<CategoryDto> categories;

    // Ticket Status History responses
    private TicketStatusHistoryDto ticketStatusHistory;
    private List<TicketStatusHistoryDto> ticketStatusHistories;

    // Ticket Message responses
    private TicketMessageDto ticketMessage;
    private List<TicketMessageDto> ticketMessages;

    // Ticket Feedback responses
    private TicketFeedbackDto ticketFeedback;
    private List<TicketFeedbackDto> ticketFeedbacks;

    // Ticket Attachment responses
    private TicketAttachmentDto ticketAttachment;
    private List<TicketAttachmentDto> ticketAttachments;

    // Knowledge Article responses
    private KnowledgeArticleDto knowledgeArticle;
    private List<KnowledgeArticleDto> knowledgeArticles;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}