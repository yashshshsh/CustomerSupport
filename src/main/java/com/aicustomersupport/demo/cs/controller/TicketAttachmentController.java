package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketAttachment;
import com.aicustomersupport.demo.cs.service.interfac.ITicketAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-attachments")
public class TicketAttachmentController {

    @Autowired
    private ITicketAttachmentService ticketAttachmentService;

    @PostMapping
    public Response createAttachment(
            @RequestBody TicketAttachment attachment) {

        return ticketAttachmentService.createAttachment(attachment);
    }

    @GetMapping("/{id}")
    public Response getAttachment(@PathVariable Long id) {

        return ticketAttachmentService.getAttachment(id);
    }

    @GetMapping
    public Response getAllAttachments() {

        return ticketAttachmentService.getAllAttachments();
    }

    @GetMapping("/ticket/{ticketId}")
    public Response getAttachmentsByTicket(
            @PathVariable Long ticketId) {

        return ticketAttachmentService
                .getAttachmentsByTicket(ticketId);
    }

    @GetMapping("/user/{userId}")
    public Response getAttachmentsByUser(
            @PathVariable Long userId) {

        return ticketAttachmentService
                .getAttachmentsByUser(userId);
    }

    @PutMapping("/{id}")
    public Response updateAttachment(
            @RequestBody TicketAttachment attachment,
            @PathVariable Long id) {

        return ticketAttachmentService
                .updateAttachment(attachment, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteAttachment(@PathVariable Long id) {

        return ticketAttachmentService.deleteAttachment(id);
    }
}