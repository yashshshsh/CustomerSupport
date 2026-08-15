package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketMessage;
import com.aicustomersupport.demo.cs.service.interfac.ITicketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-messages")
public class TicketMessageController {

    @Autowired
    private ITicketMessageService ticketMessageService;

    @PostMapping
    public Response createMessage(
            @RequestBody TicketMessage ticketMessage) {

        return ticketMessageService.createMessage(ticketMessage);
    }

    @GetMapping("/{id}")
    public Response getMessage(@PathVariable Long id) {

        return ticketMessageService.getMessage(id);
    }

    @GetMapping
    public Response getAllMessages() {

        return ticketMessageService.getAllMessages();
    }

    @GetMapping("/ticket/{ticketId}")
    public Response getMessagesByTicket(
            @PathVariable Long ticketId) {

        return ticketMessageService
                .getMessagesByTicket(ticketId);
    }

    @GetMapping("/sender/{senderId}")
    public Response getMessagesBySender(
            @PathVariable Long senderId) {

        return ticketMessageService
                .getMessagesBySender(senderId);
    }

    @PutMapping("/{id}")
    public Response updateMessage(
            @RequestBody TicketMessage ticketMessage,
            @PathVariable Long id) {

        return ticketMessageService
                .updateMessage(ticketMessage, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteMessage(@PathVariable Long id) {

        return ticketMessageService.deleteMessage(id);
    }
}