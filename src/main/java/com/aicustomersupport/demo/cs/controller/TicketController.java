package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private ITicketService ticketService;

    @PostMapping
    public Response createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @GetMapping("/{id}")
    public Response getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @GetMapping
    public Response getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/customer/{customerId}")
    public Response getTicketsByCustomer(
            @PathVariable Long customerId) {

        return ticketService.getTicketsByCustomer(customerId);
    }

    @GetMapping("/agent/{agentId}")
    public Response getTicketsByAgent(
            @PathVariable Long agentId) {

        return ticketService.getTicketsByAgent(agentId);
    }

    @GetMapping("/status/{status}")
    public Response getTicketsByStatus(
            @PathVariable String status) {

        return ticketService.getTicketsByStatus(status);
    }

    @PutMapping("/{id}")
    public Response updateTicket(
            @RequestBody Ticket ticket,
            @PathVariable Long id) {

        return ticketService.updateTicket(ticket, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteTicket(@PathVariable Long id) {
        return ticketService.deleteTicket(id);
    }
}