package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketUpdateRequestDto;
import com.aicustomersupport.demo.cs.model.*;
import com.aicustomersupport.demo.cs.repository.*;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Override
    public Response createTicket(Ticket ticket) {
        try {
            if (ticket.getStatus() == null) {
                ticket.setStatus(TicketStatus.OPEN);
            }
            if (ticket.getPriority() == null) {
                ticket.setPriority(TicketPriority.MEDIUM);
            }

            Ticket savedTicket = ticketRepository.save(ticket);
            return Response.builder()
                    .statusCode(200)
                    .message("Ticket created successfully")
                    .ticket(savedTicket)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while creating ticket: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getTicket(Long id) {
        try {
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            if (ticketOptional.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Ticket retrieved successfully")
                        .ticket(ticketOptional.get())
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("Ticket not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while retrieving ticket: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getAllTickets() {
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            return Response.builder()
                    .statusCode(200)
                    .message("Tickets fetched successfully")
                    .tickets(tickets)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while fetching tickets: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getTicketsByCustomer(Long customerId) {
        try {
            List<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
            return Response.builder()
                    .statusCode(200)
                    .message("Customer tickets fetched successfully")
                    .tickets(tickets)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while fetching customer tickets: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getTicketsByAgent(Long agentId) {
        try {
            List<Ticket> tickets = ticketRepository.findByAssignedAgentId(agentId);
            return Response.builder()
                    .statusCode(200)
                    .message("Agent tickets fetched successfully")
                    .tickets(tickets)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while fetching agent tickets: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getTicketsByStatus(String status) {
        try {
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            List<Ticket> tickets = ticketRepository.findByStatus(ticketStatus);
            return Response.builder()
                    .statusCode(200)
                    .message("Tickets by status fetched successfully")
                    .tickets(tickets)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.builder()
                    .statusCode(400)
                    .message("Invalid ticket status: " + status)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while fetching tickets by status: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateTicket(TicketUpdateRequestDto request, Long id) {
        try {
            Optional<Ticket> existingTicketOptional = ticketRepository.findById(id);
            if (existingTicketOptional.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message("Ticket not found with id: " + id)
                        .build();
            }

            Ticket existingTicket = existingTicketOptional.get();

            // 1. Update Basic Fields
            if (request.getSubject() != null) {
                existingTicket.setSubject(request.getSubject());
            }
            if (request.getDescription() != null) {
                existingTicket.setDescription(request.getDescription());
            }
            if (request.getPriority() != null) {
                existingTicket.setPriority(request.getPriority());
            }

            // 2. Update Customer Relationship (if provided)
            if (request.getCustomerId() != null) {
                Optional<User> customerOpt = userRepository.findById(request.getCustomerId());
                if (customerOpt.isEmpty()) {
                    return Response.builder()
                            .statusCode(400)
                            .message("Customer not found with id: " + request.getCustomerId())
                            .build();
                }
                existingTicket.setCustomer(customerOpt.get());
            }

            // 3. Update Assigned Agent Relationship (if provided)
            if (request.getAssignedAgentId() != null) {
                Optional<User> agentOpt = userRepository.findById(request.getAssignedAgentId());
                if (agentOpt.isEmpty()) {
                    return Response.builder()
                            .statusCode(400)
                            .message("Agent not found with id: " + request.getAssignedAgentId())
                            .build();
                }
                existingTicket.setAssignedAgent(agentOpt.get());
            }

            // 4. Update Category Relationship (if provided)
            if (request.getCategoryId() != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
                if (categoryOpt.isEmpty()) {
                    return Response.builder()
                            .statusCode(400)
                            .message("Category not found with id: " + request.getCategoryId())
                            .build();
                }
                existingTicket.setCategory(categoryOpt.get());
            }

            // 5. Status Update & Strict Validation for TicketStatusHistory
            if (request.getStatus() != null && !request.getStatus().equals(existingTicket.getStatus())) {

                // Strict validation: changedByUserId must be present when changing status
                if (request.getChangedByUserId() == null) {
                    return Response.builder()
                            .statusCode(400)
                            .message("changedByUserId is required when changing ticket status")
                            .build();
                }

                Optional<User> changedByUserOpt = userRepository.findById(request.getChangedByUserId());
                if (changedByUserOpt.isEmpty()) {
                    return Response.builder()
                            .statusCode(400)
                            .message("User who changed the status was not found with id: " + request.getChangedByUserId())
                            .build();
                }

                TicketStatus oldStatus = existingTicket.getStatus();
                TicketStatus newStatus = request.getStatus();

                existingTicket.setStatus(newStatus);

                TicketStatusHistory history = TicketStatusHistory.builder()
                        .ticket(existingTicket)
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .changedBy(changedByUserOpt.get())
                        .build();

                ticketStatusHistoryRepository.save(history);
            }

            Ticket savedTicket = ticketRepository.save(existingTicket);
            return Response.builder()
                    .statusCode(200)
                    .message("Ticket updated successfully")
                    .ticket(savedTicket)
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while updating ticket: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteTicket(Long id) {
        try {
            if (ticketRepository.existsById(id)) {
                ticketRepository.deleteById(id);
                return Response.builder()
                        .statusCode(200)
                        .message("Ticket deleted successfully")
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("Ticket not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error occurred while deleting ticket: " + e.getMessage())
                    .build();
        }
    }
}