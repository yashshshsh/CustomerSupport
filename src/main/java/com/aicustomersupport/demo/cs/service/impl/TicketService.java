package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketDto;
import com.aicustomersupport.demo.cs.dto.TicketUpdateRequestDto;
import com.aicustomersupport.demo.cs.model.Category;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Response createTicket(Ticket ticket) {

        Response response = new Response();

        try {

            Ticket savedTicket = ticketRepository.save(ticket);

            response.setStatusCode(200);
            response.setMessage("Ticket created successfully");
            response.setTicket(convertToDto(savedTicket));

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating ticket: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getTicket(Long id) {

        Response response = new Response();

        try {

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Ticket not found");

                return response;
            }

            response.setStatusCode(200);
            response.setMessage("Ticket retrieved successfully");
            response.setTicket(
                    convertToDto(ticketOptional.get())
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket: " + e.getMessage()
            );
        }

        return response;
    }

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
            response.setMessage("Tickets retrieved successfully");
            response.setTickets(ticketDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting tickets: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getTicketsByCustomer(Long customerId) {

        Response response = new Response();

        try {

            List<Ticket> tickets =
                    ticketRepository.findByCustomerId(customerId);

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
                    "Error while getting customer tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getTicketsByAgent(Long agentId) {

        Response response = new Response();

        try {

            List<Ticket> tickets =
                    ticketRepository.findByAssignedAgentId(agentId);

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
                    "Error while getting agent tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getTicketsByStatus(String status) {

        Response response = new Response();

        try {

            TicketStatus ticketStatus =
                    TicketStatus.valueOf(status.toUpperCase());

            List<Ticket> tickets =
                    ticketRepository.findByStatus(ticketStatus);

            List<TicketDto> ticketDtos =
                    tickets.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Tickets retrieved successfully"
            );
            response.setTickets(ticketDtos);

        } catch (IllegalArgumentException e) {

            response.setStatusCode(400);
            response.setMessage("Invalid ticket status");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting tickets: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateTicket(
            TicketUpdateRequestDto request,
            Long id) {

        Response response = new Response();

        try {

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Ticket not found");

                return response;
            }

            Ticket existingTicket =
                    ticketOptional.get();

            if (request.getSubject() != null) {
                existingTicket.setSubject(
                        request.getSubject()
                );
            }

            if (request.getDescription() != null) {
                existingTicket.setDescription(
                        request.getDescription()
                );
            }

            if (request.getPriority() != null) {
                existingTicket.setPriority(
                        request.getPriority()
                );
            }

            if (request.getStatus() != null &&
                    !request.getStatus().equals(
                            existingTicket.getStatus()
                    )) {

                if (request.getChangedByUserId() == null) {

                    response.setStatusCode(400);
                    response.setMessage(
                            "changedByUserId is required when changing ticket status"
                    );

                    return response;
                }

                Optional<User> changedByUserOptional =
                        userRepository.findById(
                                request.getChangedByUserId()
                        );

                if (changedByUserOptional.isEmpty()) {

                    response.setStatusCode(400);
                    response.setMessage(
                            "User who changed the status was not found"
                    );

                    return response;
                }

                TicketStatus oldStatus =
                        existingTicket.getStatus();

                TicketStatus newStatus =
                        request.getStatus();

                existingTicket.setStatus(newStatus);

                TicketStatusHistory history =
                        TicketStatusHistory.builder()
                                .ticket(existingTicket)
                                .oldStatus(oldStatus)
                                .newStatus(newStatus)
                                .changedBy(
                                        changedByUserOptional.get()
                                )
                                .build();

                ticketStatusHistoryRepository.save(history);
            }

            if (request.getCustomerId() != null) {

                Optional<User> customerOptional =
                        userRepository.findById(
                                request.getCustomerId()
                        );

                if (customerOptional.isEmpty()) {

                    response.setStatusCode(400);
                    response.setMessage("Customer not found");

                    return response;
                }

                existingTicket.setCustomer(
                        customerOptional.get()
                );
            }

            if (request.getAssignedAgentId() != null) {

                Optional<User> agentOptional =
                        userRepository.findById(
                                request.getAssignedAgentId()
                        );

                if (agentOptional.isEmpty()) {

                    response.setStatusCode(400);
                    response.setMessage(
                            "Assigned agent not found"
                    );

                    return response;
                }

                existingTicket.setAssignedAgent(
                        agentOptional.get()
                );
            }

            if (request.getCategoryId() != null) {

                Optional<Category> categoryOptional =
                        categoryRepository.findById(
                                request.getCategoryId()
                        );

                if (categoryOptional.isEmpty()) {

                    response.setStatusCode(400);
                    response.setMessage("Category not found");

                    return response;
                }

                existingTicket.setCategory(
                        categoryOptional.get()
                );
            }

            Ticket updatedTicket =
                    ticketRepository.save(existingTicket);

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
                    "Error while updating ticket: " + e.getMessage()
            );
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
            response.setMessage(
                    "Ticket deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting ticket: " + e.getMessage()
            );
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