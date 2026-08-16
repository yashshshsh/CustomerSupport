package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketDto;
import com.aicustomersupport.demo.cs.dto.TicketUpdateRequestDto;
import com.aicustomersupport.demo.cs.model.Category;
import com.aicustomersupport.demo.cs.model.Role;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketStatus;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.CategoryRepository;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
import com.aicustomersupport.demo.cs.repository.TicketStatusHistoryRepository;
import com.aicustomersupport.demo.cs.repository.UserRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aicustomersupport.demo.cs.serviceai.AiClassificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AiClassificationService aiClassificationService;

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;


    @Override
    @Transactional
    public Response createTicket(Ticket ticket) {
        Response response = new Response();
        try {
            if (ticket.getSubject() == null || ticket.getSubject().isBlank()) {
                response.setStatusCode(400);
                response.setMessage("Subject is required");
                return response;
            }

            if (ticket.getCustomer() == null || ticket.getCustomer().getId() == null) {
                response.setStatusCode(400);
                response.setMessage("Customer ID is required");
                return response;
            }

            Optional<User> customerOptional = userRepository.findById(ticket.getCustomer().getId());
            if (customerOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Customer not found");
                return response;
            }

            User customer = customerOptional.get();
            if (customer.getRole() != Role.CUSTOMER) {
                response.setStatusCode(400);
                response.setMessage("Selected user is not a customer");
                return response;
            }

            User assignedAgent = null;
            if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getId() != null) {
                Optional<User> agentOptional = userRepository.findById(ticket.getAssignedAgent().getId());
                if (agentOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("Assigned agent not found");
                    return response;
                }

                assignedAgent = agentOptional.get();
                if (assignedAgent.getRole() != Role.AGENT) {
                    response.setStatusCode(400);
                    response.setMessage("Selected user is not an agent");
                    return response;
                }
            }

            Category category = null;
            if (ticket.getCategory() != null && ticket.getCategory().getId() != null) {
                Optional<Category> categoryOptional = categoryRepository.findById(ticket.getCategory().getId());
                if (categoryOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("Category not found");
                    return response;
                }
                category = categoryOptional.get();
            }

            ticket.setCustomer(customer);
            ticket.setAssignedAgent(assignedAgent);
            ticket.setCategory(category);

            if (ticket.getStatus() == null) {
                ticket.setStatus(TicketStatus.OPEN);
            }

            String text = ticket.getSubject() + " " + ticket.getDescription();

            Map<String, Object> aiResult =
                    aiClassificationService.classifyTicket(text);

            String predictedCategory =
                    (String) aiResult.get("category");

            Double confidence =
                    ((Number) aiResult.get("confidence")).doubleValue();

            System.out.println("AI Predicted Category: " + predictedCategory);
            System.out.println("AI Confidence: " + confidence);

            Optional<Category> category1 =
                    categoryRepository.findByName(predictedCategory);

            if (category1.isPresent()) {
                ticket.setCategory(category1.get());
            }

            Ticket savedTicket = ticketRepository.save(ticket);

            response.setStatusCode(200);
            response.setMessage("Ticket created successfully");
            response.setTicket(convertToDto(savedTicket));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while creating ticket: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getTicket(Long id) {
        Response response = new Response();
        try {
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            if (ticketOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Ticket not found");
                return response;
            }

            response.setStatusCode(200);
            response.setMessage("Ticket retrieved successfully");
            response.setTicket(convertToDto(ticketOptional.get()));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while fetching ticket: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllTickets() {
        Response response = new Response();
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            List<TicketDto> ticketDtos = tickets.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Tickets retrieved successfully");
            response.setTickets(ticketDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while fetching tickets: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getTicketsByCustomer(Long customerId) {
        Response response = new Response();
        try {
            if (!userRepository.existsById(customerId)) {
                response.setStatusCode(404);
                response.setMessage("Customer not found");
                return response;
            }

            List<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
            List<TicketDto> ticketDtos = tickets.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Customer tickets retrieved successfully");
            response.setTickets(ticketDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while fetching customer tickets: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getTicketsByAgent(Long agentId) {
        Response response = new Response();
        try {
            if (!userRepository.existsById(agentId)) {
                response.setStatusCode(404);
                response.setMessage("Agent not found");
                return response;
            }

            List<Ticket> tickets = ticketRepository.findByAssignedAgentId(agentId);
            List<TicketDto> ticketDtos = tickets.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Agent tickets retrieved successfully");
            response.setTickets(ticketDtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while fetching agent tickets: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getTicketsByStatus(String status) {
        Response response = new Response();
        try {
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            List<Ticket> tickets = ticketRepository.findByStatus(ticketStatus);
            List<TicketDto> ticketDtos = tickets.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Tickets retrieved successfully by status");
            response.setTickets(ticketDtos);
        } catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setMessage("Invalid ticket status: " + status);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while fetching tickets by status: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateTicket(TicketUpdateRequestDto ticketUpdateRequest, Long id) {
        Response response = new Response();
        try {
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            if (ticketOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Ticket not found");
                return response;
            }

            Ticket ticket = ticketOptional.get();

            if (ticketUpdateRequest.getSubject() != null && !ticketUpdateRequest.getSubject().isBlank()) {
                ticket.setSubject(ticketUpdateRequest.getSubject());
            }

            if (ticketUpdateRequest.getDescription() != null && !ticketUpdateRequest.getDescription().isBlank()) {
                ticket.setDescription(ticketUpdateRequest.getDescription());
            }

            if (ticketUpdateRequest.getPriority() != null) {
                ticket.setPriority(ticketUpdateRequest.getPriority());
            }

            if (ticketUpdateRequest.getAssignedAgentId() != null) {
                Optional<User> agentOptional = userRepository.findById(ticketUpdateRequest.getAssignedAgentId());
                if (agentOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("Assigned agent not found");
                    return response;
                }

                User agent = agentOptional.get();
                if (agent.getRole() != Role.AGENT) {
                    response.setStatusCode(400);
                    response.setMessage("Selected user is not an agent");
                    return response;
                }
                ticket.setAssignedAgent(agent);
            }

            if (ticketUpdateRequest.getCategoryId() != null) {
                Optional<Category> categoryOptional = categoryRepository.findById(ticketUpdateRequest.getCategoryId());
                if (categoryOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("Category not found");
                    return response;
                }
                ticket.setCategory(categoryOptional.get());
            }

            if (ticketUpdateRequest.getStatus() != null && ticketUpdateRequest.getStatus() != ticket.getStatus()) {
                if (ticketUpdateRequest.getChangedByUserId() == null) {
                    response.setStatusCode(400);
                    response.setMessage("User ID changing the status is required");
                    return response;
                }

                Optional<User> changedByUserOptional = userRepository.findById(ticketUpdateRequest.getChangedByUserId());
                if (changedByUserOptional.isEmpty()) {
                    response.setStatusCode(404);
                    response.setMessage("User changing status not found");
                    return response;
                }

                TicketStatus oldStatus = ticket.getStatus();
                TicketStatus newStatus = ticketUpdateRequest.getStatus();

                ticket.setStatus(newStatus);
                if (newStatus == TicketStatus.RESOLVED || newStatus == TicketStatus.CLOSED) {
                    ticket.setResolvedAt(LocalDateTime.now());
                }

                TicketStatusHistory history = new TicketStatusHistory();
                history.setTicket(ticket);
                history.setOldStatus(oldStatus);
                history.setNewStatus(newStatus);
                history.setChangedBy(changedByUserOptional.get());
                ticketStatusHistoryRepository.save(history);
            }

            Ticket updatedTicket = ticketRepository.save(ticket);

            response.setStatusCode(200);
            response.setMessage("Ticket updated successfully");
            response.setTicket(convertToDto(updatedTicket));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while updating ticket: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteTicket(Long id) {
        Response response = new Response();
        try {
            if (!ticketRepository.existsById(id)) {
                response.setStatusCode(404);
                response.setMessage("Ticket not found");
                return response;
            }

            ticketRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("Ticket deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error while deleting ticket: " + e.getMessage());
        }
        return response;
    }

    private TicketDto convertToDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCustomer() != null ? ticket.getCustomer().getId() : null,
                ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId() : null,
                ticket.getCategory() != null ? ticket.getCategory().getId() : null,
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}