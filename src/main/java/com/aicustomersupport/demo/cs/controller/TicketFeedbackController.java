package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketFeedback;
import com.aicustomersupport.demo.cs.service.interfac.ITicketFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-feedback")
public class TicketFeedbackController {

    @Autowired
    private ITicketFeedbackService ticketFeedbackService;

    @PostMapping
    public Response createFeedback(
            @RequestBody TicketFeedback feedback) {

        return ticketFeedbackService.createFeedback(feedback);
    }

    @GetMapping("/{id}")
    public Response getFeedback(@PathVariable Long id) {

        return ticketFeedbackService.getFeedback(id);
    }

    @GetMapping
    public Response getAllFeedback() {

        return ticketFeedbackService.getAllFeedback();
    }

    @GetMapping("/ticket/{ticketId}")
    public Response getFeedbackByTicket(
            @PathVariable Long ticketId) {

        return ticketFeedbackService
                .getFeedbackByTicket(ticketId);
    }

    @PutMapping("/{id}")
    public Response updateFeedback(
            @RequestBody TicketFeedback feedback,
            @PathVariable Long id) {

        return ticketFeedbackService
                .updateFeedback(feedback, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteFeedback(@PathVariable Long id) {

        return ticketFeedbackService.deleteFeedback(id);
    }
}