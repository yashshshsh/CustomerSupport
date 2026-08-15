package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketStatus;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Response createTicket(Ticket ticket) {

        Response response = new Response();

        try {

            Ticket savedTicket = ticketRepository.save(ticket);

            response.setStatusCode(200);
            response.setMessage("Ticket created successfully");
            response.setTicket(savedTicket);

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

                response.setStatusCode(400);
                response.setMessage("Ticket not found");

                return response;
            }

            response.setStatusCode(200);
            response.setTicket(ticketOptional.get());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting ticket: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllTickets() {

        Response response = new Response();

        try {

            List<Ticket> tickets = ticketRepository.findAll();

            response.setStatusCode(200);
            response.setMessage("Tickets retrieved successfully");
            response.setTickets(tickets);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting tickets: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getTicketsByCustomer(Long customerId) {

        Response response = new Response();

        try {

            List<Ticket> tickets =
                    ticketRepository.findByCustomerId(customerId);

            response.setStatusCode(200);
            response.setMessage("Customer tickets retrieved successfully");
            response.setTickets(tickets);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting customer tickets: "
                    + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getTicketsByAgent(Long agentId) {

        Response response = new Response();

        try {

            List<Ticket> tickets =
                    ticketRepository.findByAssignedAgentId(agentId);

            response.setStatusCode(200);
            response.setMessage("Agent tickets retrieved successfully");
            response.setTickets(tickets);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting agent tickets: "
                    + e.getMessage());
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

            response.setStatusCode(200);
            response.setMessage("Tickets retrieved successfully");
            response.setTickets(tickets);

        } catch (IllegalArgumentException e) {

            response.setStatusCode(400);
            response.setMessage("Invalid ticket status");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting tickets: "
                    + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateTicket(Ticket ticket, Long id) {

        Response response = new Response();

        try {

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(id);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Ticket not found");

                return response;
            }

            Ticket existingTicket = ticketOptional.get();

            if (ticket.getSubject() != null) {
                existingTicket.setSubject(ticket.getSubject());
            }

            if (ticket.getDescription() != null) {
                existingTicket.setDescription(ticket.getDescription());
            }

            if (ticket.getStatus() != null) {
                existingTicket.setStatus(ticket.getStatus());
            }

            if (ticket.getPriority() != null) {
                existingTicket.setPriority(ticket.getPriority());
            }

            if (ticket.getCustomer() != null) {
                existingTicket.setCustomer(ticket.getCustomer());
            }

            if (ticket.getAssignedAgent() != null) {
                existingTicket.setAssignedAgent(ticket.getAssignedAgent());
            }

            if (ticket.getCategory() != null) {
                existingTicket.setCategory(ticket.getCategory());
            }

            if (ticket.getResolvedAt() != null) {
                existingTicket.setResolvedAt(ticket.getResolvedAt());
            }

            Ticket updatedTicket =
                    ticketRepository.save(existingTicket);

            response.setStatusCode(200);
            response.setMessage("Ticket updated successfully");
            response.setTicket(updatedTicket);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while updating ticket: "
                    + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteTicket(Long id) {

        Response response = new Response();

        try {

            if (!ticketRepository.existsById(id)) {

                response.setStatusCode(400);
                response.setMessage("Ticket not found");

                return response;
            }

            ticketRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("Ticket deleted successfully");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while deleting ticket: "
                    + e.getMessage());
        }

        return response;
    }
}