package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.*;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.Role;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketPriority;
import com.aicustomersupport.demo.cs.model.TicketStatus;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
import com.aicustomersupport.demo.cs.repository.TicketStatusHistoryRepository;
import com.aicustomersupport.demo.cs.repository.UserRepository;
import com.aicustomersupport.demo.cs.service.interfac.IAiDecisionService;
import com.aicustomersupport.demo.cs.service.interfac.IAiTicketAnalysisService;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IAiTicketAnalysisService aiTicketAnalysisService;

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Autowired
    private IAiDecisionService aiDecisionService;

    @Override
    public Response makeAIDecision(Long id) {

        Response response = new Response();

        try {

            // ====================================================
            // FIND EXISTING TICKET
            // ====================================================

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }


            Ticket ticket =
                    ticketOptional.get();


            // ====================================================
            // BUILD TEXT FOR AI
            // ====================================================

            String text =
                    (
                            ticket.getSubject() != null
                                    ? ticket.getSubject()
                                    : ""
                    )
                            + " "
                            + (
                            ticket.getDescription() != null
                                    ? ticket.getDescription()
                                    : ""
                    );


            // ====================================================
            // RUN AI DECISION ENGINE
            // ====================================================

            AiDecisionDto decision =
                    aiDecisionService.makeDecision(
                            text
                    );


            // ====================================================
            // BUILD RESPONSE
            // ====================================================

            response.setStatusCode(200);

            response.setMessage(
                    "AI decision generated successfully"
            );


            // ====================================================
            // AI CATEGORY
            // ====================================================

            response.setAiCategory(
                    decision.getAiCategory()
            );

            response.setAiCategoryConfidence(
                    decision.getAiCategoryConfidence()
            );


            // ====================================================
            // AI PRIORITY
            // ====================================================

            response.setAiPriority(
                    decision.getAiPriority()
            );

            response.setAiPriorityConfidence(
                    decision.getAiPriorityConfidence()
            );


            // ====================================================
            // SUGGESTED ACTION
            // ====================================================

            response.setSuggestedAction(
                    decision.getSuggestedAction()
            );


            // ====================================================
            // DECISION REASON
            // ====================================================

            response.setDecisionReason(
                    decision.getReason()
            );


            // ====================================================
            // KNOWLEDGE ARTICLES
            // ====================================================

            response.setKnowledgeArticleRecommendations(
                    decision.getKnowledgeArticleRecommendations()
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.setStatusCode(500);

            response.setMessage(
                    "Error while generating AI decision: "
                            + e.getMessage()
            );
        }

        return response;
    }

    // ============================================================
// AI RE-ANALYSIS OF EXISTING TICKET
// ============================================================

    // ============================================================
// APPLY AI ANALYSIS TO EXISTING TICKET
// ============================================================

    @Override
    @Transactional
    public Response applyAIAnalysis(Long id) {

        Response response = new Response();

        try {

            // ====================================================
            // FIND EXISTING TICKET
            // ====================================================

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }

            Ticket ticket =
                    ticketOptional.get();


            // ====================================================
            // BUILD TEXT FOR AI
            // ====================================================

            String text =
                    ticket.getSubject()
                            + " "
                            + (
                            ticket.getDescription() != null
                                    ? ticket.getDescription()
                                    : ""
                    );


            // ====================================================
            // RUN AI ANALYSIS
            // ====================================================

            AiTicketAnalysisDto aiAnalysis =
                    aiTicketAnalysisService.analyzeTicket(
                            text
                    );


            // ====================================================
            // GET AI CATEGORY
            // ====================================================

            String predictedCategory =
                    aiAnalysis.getCategory();


            Double categoryConfidence =
                    aiAnalysis.getCategoryConfidence();


            // ====================================================
            // APPLY AI CATEGORY
            // ====================================================

            if (predictedCategory != null
                    && !predictedCategory.isBlank()) {

                Optional<Category> categoryOptional =
                        categoryRepository.findByName(
                                predictedCategory
                        );

                if (categoryOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "AI predicted category not found: "
                                    + predictedCategory
                    );

                    return response;
                }

                ticket.setCategory(
                        categoryOptional.get()
                );
            }


            // ====================================================
            // GET AI PRIORITY
            // ====================================================

            String predictedPriority =
                    aiAnalysis.getPriority();


            Double priorityConfidence =
                    aiAnalysis.getPriorityConfidence();


            // ====================================================
            // APPLY AI PRIORITY
            // ====================================================

            if (predictedPriority != null
                    && !predictedPriority.isBlank()) {

                try {

                    ticket.setPriority(
                            TicketPriority.valueOf(
                                    predictedPriority.toUpperCase()
                            )
                    );

                } catch (IllegalArgumentException e) {

                    response.setStatusCode(400);

                    response.setMessage(
                            "Invalid AI priority: "
                                    + predictedPriority
                    );

                    return response;
                }
            }


            // ====================================================
            // SAVE UPDATED TICKET
            // ====================================================

            Ticket updatedTicket =
                    ticketRepository.save(ticket);


            // ====================================================
            // BUILD RESPONSE
            // ====================================================

            response.setStatusCode(200);

            response.setMessage(
                    "AI analysis applied successfully"
            );


            // Updated ticket
            response.setTicket(
                    convertToDto(updatedTicket)
            );


            // AI results
            response.setAiCategory(
                    predictedCategory
            );

            response.setAiCategoryConfidence(
                    categoryConfidence
            );

            response.setAiPriority(
                    predictedPriority
            );

            response.setAiPriorityConfidence(
                    priorityConfidence
            );


            // Recommendations are suggestions only.
            // They are NOT saved to the ticket.
            response.setKnowledgeArticleRecommendations(
                    aiAnalysis.getRecommendations()
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.setStatusCode(500);

            response.setMessage(
                    "Error while applying AI analysis: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response analyzeTicketWithAI(Long id) {

        Response response = new Response();

        try {

            // ====================================================
            // FIND EXISTING TICKET
            // ====================================================

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }

            Ticket ticket =
                    ticketOptional.get();


            // ====================================================
            // BUILD TEXT FOR AI
            // ====================================================

            String text =
                    ticket.getSubject()
                            + " "
                            + (
                            ticket.getDescription() != null
                                    ? ticket.getDescription()
                                    : ""
                    );


            // ====================================================
            // RUN AI ANALYSIS
            // ====================================================

            AiTicketAnalysisDto aiAnalysis =
                    aiTicketAnalysisService.analyzeTicket(
                            text
                    );


            // ====================================================
            // BUILD RESPONSE
            // ====================================================

            response.setStatusCode(200);

            response.setMessage(
                    "AI ticket analysis completed successfully"
            );


            response.setAiCategory(
                    aiAnalysis.getCategory()
            );

            response.setAiCategoryConfidence(
                    aiAnalysis.getCategoryConfidence()
            );


            response.setAiPriority(
                    aiAnalysis.getPriority()
            );

            response.setAiPriorityConfidence(
                    aiAnalysis.getPriorityConfidence()
            );


            response.setKnowledgeArticleRecommendations(
                    aiAnalysis.getRecommendations()
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.setStatusCode(500);

            response.setMessage(
                    "Error while analyzing ticket with AI: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // CREATE TICKET
    // ============================================================

    @Override
    @Transactional
    public Response createTicket(Ticket ticket) {

        Response response = new Response();

        try {

            // ====================================================
            // BASIC VALIDATION
            // ====================================================

            if (ticket.getSubject() == null
                    || ticket.getSubject().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Subject is required");

                return response;
            }


            if (ticket.getCustomer() == null
                    || ticket.getCustomer().getId() == null) {

                response.setStatusCode(400);
                response.setMessage("Customer ID is required");

                return response;
            }


            // ====================================================
            // FIND CUSTOMER
            // ====================================================

            Optional<User> customerOptional =
                    userRepository.findById(
                            ticket.getCustomer().getId()
                    );

            if (customerOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Customer not found");

                return response;
            }

            User customer =
                    customerOptional.get();


            if (customer.getRole() != Role.CUSTOMER) {

                response.setStatusCode(400);

                response.setMessage(
                        "Selected user is not a customer"
                );

                return response;
            }


            // ====================================================
            // FIND ASSIGNED AGENT
            // ====================================================

            User assignedAgent = null;

            if (ticket.getAssignedAgent() != null
                    && ticket.getAssignedAgent().getId() != null) {

                Optional<User> agentOptional =
                        userRepository.findById(
                                ticket.getAssignedAgent().getId()
                        );

                if (agentOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "Assigned agent not found"
                    );

                    return response;
                }

                assignedAgent =
                        agentOptional.get();


                if (assignedAgent.getRole() != Role.AGENT) {

                    response.setStatusCode(400);

                    response.setMessage(
                            "Selected user is not an agent"
                    );

                    return response;
                }
            }


            // ====================================================
            // OPTIONAL CATEGORY FROM REQUEST
            // ====================================================

            Category category = null;

            if (ticket.getCategory() != null
                    && ticket.getCategory().getId() != null) {

                Optional<Category> categoryOptional =
                        categoryRepository.findById(
                                ticket.getCategory().getId()
                        );

                if (categoryOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "Category not found"
                    );

                    return response;
                }

                category =
                        categoryOptional.get();
            }


            // ====================================================
            // SET BASIC TICKET RELATIONSHIPS
            // ====================================================

            ticket.setCustomer(customer);

            ticket.setAssignedAgent(assignedAgent);

            ticket.setCategory(category);


            if (ticket.getStatus() == null) {

                ticket.setStatus(
                        TicketStatus.OPEN
                );
            }


            // ====================================================
            // BUILD TEXT FOR AI
            // ====================================================

            String text =
                    ticket.getSubject()
                            + " "
                            + (
                            ticket.getDescription() != null
                                    ? ticket.getDescription()
                                    : ""
                    );


            // ====================================================
            // COMPLETE AI TICKET ANALYSIS
            // ====================================================

            AiTicketAnalysisDto aiAnalysis =
                    aiTicketAnalysisService.analyzeTicket(
                            text
                    );


            // ====================================================
            // GET AI CATEGORY
            // ====================================================

            String predictedCategory =
                    aiAnalysis.getCategory();


            Double categoryConfidence =
                    aiAnalysis.getCategoryConfidence();


            // ====================================================
            // APPLY AI CATEGORY TO TICKET
            // ====================================================

            if (predictedCategory != null) {

                Optional<Category>
                        predictedCategoryOptional =
                        categoryRepository.findByName(
                                predictedCategory
                        );


                if (predictedCategoryOptional.isPresent()) {

                    ticket.setCategory(
                            predictedCategoryOptional.get()
                    );
                }
            }


            // ====================================================
            // GET AI PRIORITY
            // ====================================================

            String predictedPriority =
                    aiAnalysis.getPriority();


            Double priorityConfidence =
                    aiAnalysis.getPriorityConfidence();


            // ====================================================
            // APPLY AI PRIORITY TO TICKET
            // ====================================================

            try {

                if (predictedPriority != null) {

                    ticket.setPriority(
                            TicketPriority.valueOf(
                                    predictedPriority.toUpperCase()
                            )
                    );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Invalid AI priority: "
                                + predictedPriority
                );

                ticket.setPriority(
                        TicketPriority.MEDIUM
                );
            }


            // ====================================================
            // GET KNOWLEDGE RECOMMENDATIONS
            // ====================================================

            var recommendations =
                    aiAnalysis.getRecommendations();


            // ====================================================
            // SAVE TICKET
            // ====================================================

            Ticket savedTicket =
                    ticketRepository.save(ticket);


            // ====================================================
            // BUILD RESPONSE
            // ====================================================

            response.setStatusCode(200);

            response.setMessage(
                    "Ticket created successfully"
            );


            response.setTicket(
                    convertToDto(savedTicket)
            );


            // ====================================================
            // AI RESULTS
            // ====================================================

            response.setAiCategory(
                    predictedCategory
            );


            response.setAiCategoryConfidence(
                    categoryConfidence
            );


            response.setAiPriority(
                    predictedPriority
            );


            response.setAiPriorityConfidence(
                    priorityConfidence
            );


            response.setKnowledgeArticleRecommendations(
                    recommendations
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.setStatusCode(500);

            response.setMessage(
                    "Error while creating ticket: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // GET TICKET
    // ============================================================

    @Override
    public Response getTicket(Long id) {

        Response response = new Response();

        try {

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);


            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }


            response.setStatusCode(200);

            response.setMessage(
                    "Ticket retrieved successfully"
            );

            response.setTicket(
                    convertToDto(
                            ticketOptional.get()
                    )
            );


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while fetching ticket: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // GET ALL TICKETS
    // ============================================================

    @Override
    public Response getAllTickets() {

        Response response = new Response();

        try {

            List<Ticket> tickets =
                    ticketRepository.findAll();


            List<TicketDto> ticketDtos =
                    tickets.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());


            response.setStatusCode(200);

            response.setMessage(
                    "Tickets retrieved successfully"
            );

            response.setTickets(ticketDtos);


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while fetching tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // GET TICKETS BY CUSTOMER
    // ============================================================

    @Override
    public Response getTicketsByCustomer(
            Long customerId
    ) {

        Response response = new Response();

        try {

            if (!userRepository.existsById(customerId)) {

                response.setStatusCode(404);

                response.setMessage(
                        "Customer not found"
                );

                return response;
            }


            List<Ticket> tickets =
                    ticketRepository.findByCustomerId(
                            customerId
                    );


            List<TicketDto> ticketDtos =
                    tickets.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());


            response.setStatusCode(200);

            response.setMessage(
                    "Customer tickets retrieved successfully"
            );

            response.setTickets(ticketDtos);


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while fetching customer tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // GET TICKETS BY AGENT
    // ============================================================

    @Override
    public Response getTicketsByAgent(
            Long agentId
    ) {

        Response response = new Response();

        try {

            if (!userRepository.existsById(agentId)) {

                response.setStatusCode(404);

                response.setMessage(
                        "Agent not found"
                );

                return response;
            }


            List<Ticket> tickets =
                    ticketRepository.findByAssignedAgentId(
                            agentId
                    );


            List<TicketDto> ticketDtos =
                    tickets.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());


            response.setStatusCode(200);

            response.setMessage(
                    "Agent tickets retrieved successfully"
            );

            response.setTickets(ticketDtos);


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while fetching agent tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // GET TICKETS BY STATUS
    // ============================================================

    @Override
    public Response getTicketsByStatus(
            String status
    ) {

        Response response = new Response();

        try {

            TicketStatus ticketStatus =
                    TicketStatus.valueOf(
                            status.toUpperCase()
                    );


            List<Ticket> tickets =
                    ticketRepository.findByStatus(
                            ticketStatus
                    );


            List<TicketDto> ticketDtos =
                    tickets.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());


            response.setStatusCode(200);

            response.setMessage(
                    "Tickets retrieved successfully by status"
            );

            response.setTickets(ticketDtos);


        } catch (IllegalArgumentException e) {

            response.setStatusCode(400);

            response.setMessage(
                    "Invalid ticket status: "
                            + status
            );


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while fetching tickets by status: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // UPDATE TICKET
    // ============================================================

    @Override
    @Transactional
    public Response updateTicket(
            TicketUpdateRequestDto ticketUpdateRequest,
            Long id
    ) {

        Response response = new Response();

        try {

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);


            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }


            Ticket ticket =
                    ticketOptional.get();


            // ====================================================
            // SUBJECT
            // ====================================================

            if (ticketUpdateRequest.getSubject() != null
                    && !ticketUpdateRequest
                    .getSubject()
                    .isBlank()) {

                ticket.setSubject(
                        ticketUpdateRequest.getSubject()
                );
            }


            // ====================================================
            // DESCRIPTION
            // ====================================================

            if (ticketUpdateRequest.getDescription() != null
                    && !ticketUpdateRequest
                    .getDescription()
                    .isBlank()) {

                ticket.setDescription(
                        ticketUpdateRequest.getDescription()
                );
            }


            // ====================================================
            // PRIORITY
            // ====================================================

            if (ticketUpdateRequest.getPriority() != null) {

                ticket.setPriority(
                        ticketUpdateRequest.getPriority()
                );
            }


            // ====================================================
            // ASSIGNED AGENT
            // ====================================================

            if (ticketUpdateRequest
                    .getAssignedAgentId() != null) {

                Optional<User> agentOptional =
                        userRepository.findById(
                                ticketUpdateRequest
                                        .getAssignedAgentId()
                        );


                if (agentOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "Assigned agent not found"
                    );

                    return response;
                }


                User agent =
                        agentOptional.get();


                if (agent.getRole() != Role.AGENT) {

                    response.setStatusCode(400);

                    response.setMessage(
                            "Selected user is not an agent"
                    );

                    return response;
                }


                ticket.setAssignedAgent(agent);
            }


            // ====================================================
            // CATEGORY
            // ====================================================

            if (ticketUpdateRequest
                    .getCategoryId() != null) {

                Optional<Category>
                        categoryOptional =
                        categoryRepository.findById(
                                ticketUpdateRequest
                                        .getCategoryId()
                        );


                if (categoryOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "Category not found"
                    );

                    return response;
                }


                ticket.setCategory(
                        categoryOptional.get()
                );
            }


            // ====================================================
            // STATUS
            // ====================================================

            if (ticketUpdateRequest.getStatus() != null
                    && ticketUpdateRequest.getStatus()
                    != ticket.getStatus()) {


                if (ticketUpdateRequest
                        .getChangedByUserId() == null) {

                    response.setStatusCode(400);

                    response.setMessage(
                            "User ID changing the status is required"
                    );

                    return response;
                }


                Optional<User>
                        changedByUserOptional =
                        userRepository.findById(
                                ticketUpdateRequest
                                        .getChangedByUserId()
                        );


                if (changedByUserOptional.isEmpty()) {

                    response.setStatusCode(404);

                    response.setMessage(
                            "User changing status not found"
                    );

                    return response;
                }


                TicketStatus oldStatus =
                        ticket.getStatus();


                TicketStatus newStatus =
                        ticketUpdateRequest.getStatus();


                ticket.setStatus(newStatus);


                if (newStatus == TicketStatus.RESOLVED
                        || newStatus == TicketStatus.CLOSED) {

                    ticket.setResolvedAt(
                            LocalDateTime.now()
                    );
                }


                TicketStatusHistory history =
                        new TicketStatusHistory();


                history.setTicket(ticket);

                history.setOldStatus(oldStatus);

                history.setNewStatus(newStatus);

                history.setChangedBy(
                        changedByUserOptional.get()
                );


                ticketStatusHistoryRepository.save(
                        history
                );
            }


            // ====================================================
            // SAVE UPDATED TICKET
            // ====================================================

            Ticket updatedTicket =
                    ticketRepository.save(ticket);


            response.setStatusCode(200);

            response.setMessage(
                    "Ticket updated successfully"
            );

            response.setTicket(
                    convertToDto(updatedTicket)
            );


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while updating ticket: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // DELETE TICKET
    // ============================================================

    @Override
    public Response deleteTicket(Long id) {

        Response response = new Response();

        try {

            if (!ticketRepository.existsById(id)) {

                response.setStatusCode(404);

                response.setMessage(
                        "Ticket not found"
                );

                return response;
            }


            ticketRepository.deleteById(id);


            response.setStatusCode(200);

            response.setMessage(
                    "Ticket deleted successfully"
            );


        } catch (Exception e) {

            response.setStatusCode(500);

            response.setMessage(
                    "Error while deleting ticket: "
                            + e.getMessage()
            );
        }

        return response;
    }


    // ============================================================
    // CONVERT TICKET TO DTO
    // ============================================================

    private TicketDto convertToDto(
            Ticket ticket
    ) {

        return new TicketDto(

                ticket.getId(),

                ticket.getSubject(),

                ticket.getDescription(),

                ticket.getStatus(),

                ticket.getPriority(),

                ticket.getCustomer() != null
                        ? ticket.getCustomer().getId()
                        : null,

                ticket.getAssignedAgent() != null
                        ? ticket.getAssignedAgent().getId()
                        : null,

                ticket.getCategory() != null
                        ? ticket.getCategory().getId()
                        : null,

                ticket.getCreatedAt(),

                ticket.getUpdatedAt()
        );
    }
}