package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketUpdateRequestDto;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.service.interfac.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aicustomersupport.demo.cs.serviceai.AiClassificationService;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private AiClassificationService aiClassificationService;


    // ============================================================
    // DIRECT AI CATEGORY CLASSIFICATION
    // ============================================================

    @PostMapping("/ai-classify")
    public Map<String, Object> classifyTicket(
            @RequestBody Map<String, String> request) {

        return aiClassificationService.classifyTicket(
                request.get("text")
        );
    }


    // ============================================================
    // CREATE TICKET
    // ============================================================

    @PostMapping
    public Response createTicket(
            @RequestBody Ticket ticket) {

        return ticketService.createTicket(ticket);
    }


    // ============================================================
    // AI RE-ANALYSIS OF EXISTING TICKET
    // ============================================================

    @PostMapping("/{id}/ai-analysis")
    public Response analyzeTicketWithAI(
            @PathVariable Long id) {

        return ticketService.analyzeTicketWithAI(id);
    }


    // ============================================================
    // GET TICKET
    // ============================================================

    @GetMapping("/{id}")
    public Response getTicket(
            @PathVariable Long id) {

        return ticketService.getTicket(id);
    }


    // ============================================================
    // GET ALL TICKETS
    // ============================================================

    @GetMapping
    public Response getAllTickets() {

        return ticketService.getAllTickets();
    }


    // ============================================================
    // GET TICKETS BY CUSTOMER
    // ============================================================

    @GetMapping("/customer/{customerId}")
    public Response getTicketsByCustomer(
            @PathVariable Long customerId) {

        return ticketService.getTicketsByCustomer(
                customerId
        );
    }


    // ============================================================
    // GET TICKETS BY AGENT
    // ============================================================

    @GetMapping("/agent/{agentId}")
    public Response getTicketsByAgent(
            @PathVariable Long agentId) {

        return ticketService.getTicketsByAgent(
                agentId
        );
    }


    // ============================================================
    // GET TICKETS BY STATUS
    // ============================================================

    @GetMapping("/status/{status}")
    public Response getTicketsByStatus(
            @PathVariable String status) {

        return ticketService.getTicketsByStatus(
                status
        );
    }


    // ============================================================
    // UPDATE TICKET
    // ============================================================

    @PutMapping("/{id}")
    public Response updateTicket(
            @RequestBody TicketUpdateRequestDto request,
            @PathVariable Long id) {

        return ticketService.updateTicket(
                request,
                id
        );
    }


    // ============================================================
    // DELETE TICKET
    // ============================================================

    @DeleteMapping("/{id}")
    public Response deleteTicket(
            @PathVariable Long id) {

        return ticketService.deleteTicket(id);
    }

    @PostMapping("/{id}/ai-analysis/apply")
    public Response applyAIAnalysis(
            @PathVariable Long id) {

        return ticketService.applyAIAnalysis(id);
    }
}