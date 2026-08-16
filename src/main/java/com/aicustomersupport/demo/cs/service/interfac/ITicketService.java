package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketUpdateRequestDto;
import com.aicustomersupport.demo.cs.model.Ticket;

public interface ITicketService {

    Response createTicket(Ticket ticket);

    Response getTicket(Long id);

    Response getAllTickets();

    Response getTicketsByCustomer(Long customerId);

    Response getTicketsByAgent(Long agentId);

    Response getTicketsByStatus(String status);

    Response analyzeTicketWithAI(Long id);

    Response applyAIAnalysis(Long id);

    // NEW
    Response makeAIDecision(Long id);

    Response updateTicket(TicketUpdateRequestDto ticketUpdateRequest,
                          Long id);

    Response deleteTicket(Long id);
}