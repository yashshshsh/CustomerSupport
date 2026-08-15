package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.Ticket;

public interface ITicketService {

    Response createTicket(Ticket ticket);

    Response getTicket(Long id);

    Response getAllTickets();

    Response getTicketsByCustomer(Long customerId);

    Response getTicketsByAgent(Long agentId);

    Response getTicketsByStatus(String status);

    Response updateTicket(Ticket ticket, Long id);

    Response deleteTicket(Long id);
}