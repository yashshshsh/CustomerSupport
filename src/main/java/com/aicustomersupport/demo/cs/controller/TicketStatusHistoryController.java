package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.service.interfac.ITicketStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-status-history")
public class TicketStatusHistoryController {

    @Autowired
    private ITicketStatusHistoryService ticketStatusHistoryService;

    @GetMapping("/{id}")
    public Response getStatusHistory(
            @PathVariable Long id) {

        return ticketStatusHistoryService
                .getStatusHistory(id);
    }

    @GetMapping
    public Response getAllStatusHistory() {

        return ticketStatusHistoryService
                .getAllStatusHistory();
    }

    @GetMapping("/ticket/{ticketId}")
    public Response getStatusHistoryByTicket(
            @PathVariable Long ticketId) {

        return ticketStatusHistoryService
                .getStatusHistoryByTicket(ticketId);
    }

    @GetMapping("/user/{userId}")
    public Response getStatusHistoryByUser(
            @PathVariable Long userId) {

        return ticketStatusHistoryService
                .getStatusHistoryByUser(userId);
    }
}