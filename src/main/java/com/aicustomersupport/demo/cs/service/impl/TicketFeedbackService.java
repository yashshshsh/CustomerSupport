package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketFeedback;
import com.aicustomersupport.demo.cs.repository.TicketFeedbackRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketFeedbackService implements ITicketFeedbackService {

    @Autowired
    private TicketFeedbackRepository ticketFeedbackRepository;

    @Override
    public Response createFeedback(TicketFeedback feedback) {

        Response response = new Response();

        try {

            if (feedback.getRating() == null ||
                    feedback.getRating() < 1 ||
                    feedback.getRating() > 5) {

                response.setStatusCode(400);
                response.setMessage("Rating must be between 1 and 5");

                return response;
            }

            if (feedback.getTicket() == null ||
                    feedback.getTicket().getId() == null) {

                response.setStatusCode(400);
                response.setMessage("Ticket is required");

                return response;
            }

            if (ticketFeedbackRepository
                    .existsByTicketId(feedback.getTicket().getId())) {

                response.setStatusCode(400);
                response.setMessage(
                        "Feedback already exists for this ticket"
                );

                return response;
            }

            TicketFeedback savedFeedback =
                    ticketFeedbackRepository.save(feedback);

            response.setStatusCode(200);
            response.setMessage("Feedback created successfully");
            response.setTicketFeedback(savedFeedback);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getFeedback(Long id) {

        Response response = new Response();

        try {

            Optional<TicketFeedback> feedbackOptional =
                    ticketFeedbackRepository.findById(id);

            if (feedbackOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Feedback not found");

                return response;
            }

            response.setStatusCode(200);
            response.setTicketFeedback(
                    feedbackOptional.get()
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllFeedback() {

        Response response = new Response();

        try {

            List<TicketFeedback> feedbackList =
                    ticketFeedbackRepository.findAll();

            response.setStatusCode(200);
            response.setMessage(
                    "Feedback retrieved successfully"
            );
            response.setTicketFeedbacks(feedbackList);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getFeedbackByTicket(Long ticketId) {

        Response response = new Response();

        try {

            Optional<TicketFeedback> feedbackOptional =
                    ticketFeedbackRepository.findByTicketId(ticketId);

            if (feedbackOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage(
                        "Feedback not found for this ticket"
                );

                return response;
            }

            response.setStatusCode(200);
            response.setTicketFeedback(
                    feedbackOptional.get()
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateFeedback(
            TicketFeedback feedback,
            Long id) {

        Response response = new Response();

        try {

            Optional<TicketFeedback> feedbackOptional =
                    ticketFeedbackRepository.findById(id);

            if (feedbackOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Feedback not found");

                return response;
            }

            TicketFeedback existingFeedback =
                    feedbackOptional.get();

            if (feedback.getRating() != null) {

                if (feedback.getRating() < 1 ||
                        feedback.getRating() > 5) {

                    response.setStatusCode(400);
                    response.setMessage(
                            "Rating must be between 1 and 5"
                    );

                    return response;
                }

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
                    ticketFeedbackRepository.save(
                            existingFeedback
                    );

            response.setStatusCode(200);
            response.setMessage("Feedback updated successfully");
            response.setTicketFeedback(updatedFeedback);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteFeedback(Long id) {

        Response response = new Response();

        try {

            if (!ticketFeedbackRepository.existsById(id)) {

                response.setStatusCode(400);
                response.setMessage("Feedback not found");

                return response;
            }

            ticketFeedbackRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage(
                    "Feedback deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting feedback: "
                            + e.getMessage()
            );
        }

        return response;
    }
}