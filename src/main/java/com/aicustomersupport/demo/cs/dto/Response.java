package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.KnowledgeArticle;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketAttachment;
import com.aicustomersupport.demo.cs.model.TicketFeedback;
import com.aicustomersupport.demo.cs.model.TicketMessage;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.model.User;
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
    private User user;
    private List<User> users;

    // Ticket responses
    private Ticket ticket;
    private List<Ticket> tickets;

    // Category responses
    private Category category;
    private List<Category> categories;

    // Ticket Status History responses
    private TicketStatusHistory ticketStatusHistory;
    private List<TicketStatusHistory> ticketStatusHistories;

    // Ticket Message responses
    private TicketMessage ticketMessage;
    private List<TicketMessage> ticketMessages;

    // Ticket Feedback responses
    private TicketFeedback ticketFeedback;
    private List<TicketFeedback> ticketFeedbacks;

    // Ticket Attachment responses
    private TicketAttachment ticketAttachment;
    private List<TicketAttachment> ticketAttachments;

    // Knowledge Article responses
    private KnowledgeArticle knowledgeArticle;
    private List<KnowledgeArticle> knowledgeArticles;

    // Convenience constructor for simple status + message responses
    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}