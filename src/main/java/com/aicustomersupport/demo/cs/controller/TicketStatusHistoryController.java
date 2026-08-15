package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.service.interfac.ITicketStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-status-history")
public class TicketStatusHistoryController {

    @Autowired
    private ITicketStatusHistoryService ticketStatusHistoryService;

    @PostMapping
    public Response createStatusHistory(
            @RequestBody TicketStatusHistory statusHistory) {

        return ticketStatusHistoryService
                .createStatusHistory(statusHistory);
    }

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

    @DeleteMapping("/{id}")
    public Response deleteStatusHistory(
            @PathVariable Long id) {

        return ticketStatusHistoryService
                .deleteStatusHistory(id);
    }
}