package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketFeedbackDto;
import com.aicustomersupport.demo.cs.model.TicketFeedback;
import com.aicustomersupport.demo.cs.repository.TicketFeedbackRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketFeedbackService implements ITicketFeedbackService {

    @Autowired
    private TicketFeedbackRepository feedbackRepository;

    @Override
    public Response createFeedback(TicketFeedback feedback) {
        try {
            if (feedback == null || feedback.getTicket() == null ||
                    feedback.getTicket().getId() == null) {
                return Response.builder()
                        .statusCode(400)
                        .message("Ticket is required")
                        .build();
            }

            Long ticketId = feedback.getTicket().getId();

            if (feedbackRepository.existsByTicketId(ticketId)) {
                return Response.builder()
                        .statusCode(400)
                        .message("Feedback already exists for this ticket")
                        .build();
            }

            TicketFeedback savedFeedback =
                    feedbackRepository.save(feedback);

            return Response.builder()
                    .statusCode(201)
                    .message("Ticket feedback created successfully")
                    .ticketFeedback(convertToDto(savedFeedback))
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error creating feedback: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getFeedback(Long id) {
        try {
            Optional<TicketFeedback> feedbackOpt =
                    feedbackRepository.findById(id);

            if (feedbackOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Feedback retrieved successfully")
                        .ticketFeedback(convertToDto(feedbackOpt.get()))
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message("Feedback not found with id: " + id)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving feedback: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getAllFeedback() {
        try {
            List<TicketFeedback> feedbacks =
                    feedbackRepository.findAll();

            List<TicketFeedbackDto> feedbackDtos =
                    feedbacks.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("All feedback retrieved successfully")
                    .ticketFeedbacks(feedbackDtos)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving all feedback: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getFeedbackByTicket(Long ticketId) {
        try {
            Optional<TicketFeedback> feedbackOpt =
                    feedbackRepository.findByTicketId(ticketId);

            if (feedbackOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Ticket feedback retrieved successfully")
                        .ticketFeedback(convertToDto(feedbackOpt.get()))
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message("Feedback not found for ticket id: " + ticketId)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving ticket feedback: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateFeedback(
            TicketFeedback feedback,
            Long id) {

        try {
            Optional<TicketFeedback> existingOpt =
                    feedbackRepository.findById(id);

            if (existingOpt.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message("Feedback not found with id: " + id)
                        .build();
            }

            TicketFeedback existingFeedback =
                    existingOpt.get();

            if (feedback.getRating() != null) {
                existingFeedback.setRating(
                        feedback.getRating()
                );
            }

            if (feedback.getComment() != null) {
                existingFeedback.setComment(
                        feedback.getComment()
                );
            }

            TicketFeedback updatedFeedback =
                    feedbackRepository.save(existingFeedback);

            return Response.builder()
                    .statusCode(200)
                    .message("Feedback updated successfully")
                    .ticketFeedback(
                            convertToDto(updatedFeedback)
                    )
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error updating feedback: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteFeedback(Long id) {
        try {
            if (!feedbackRepository.existsById(id)) {
                return Response.builder()
                        .statusCode(404)
                        .message("Feedback not found with id: " + id)
                        .build();
            }

            feedbackRepository.deleteById(id);

            return Response.builder()
                    .statusCode(200)
                    .message("Feedback deleted successfully")
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error deleting feedback: " + e.getMessage())
                    .build();
        }
    }

    private TicketFeedbackDto convertToDto(
            TicketFeedback feedback) {

        return TicketFeedbackDto.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .ticketId(
                        feedback.getTicket() != null
                                ? feedback.getTicket().getId()
                                : null
                )
                .customerId(
                        feedback.getCustomer() != null
                                ? feedback.getCustomer().getId()
                                : null
                )
                .build();
    }
}