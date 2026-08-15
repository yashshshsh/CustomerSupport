package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.service.interfac.ITicketAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ticket-attachments")
public class TicketAttachmentController {

    @Autowired
    private ITicketAttachmentService attachmentService;

    @PostMapping("/upload")
    public Response uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ticketId") Long ticketId,
            @RequestParam("userId") Long userId) {
        return attachmentService.uploadAttachment(file, ticketId, userId);
    }

    @GetMapping("/{id}")
    public Response getAttachment(@PathVariable Long id) {
        return attachmentService.getAttachment(id);
    }

    @GetMapping
    public Response getAllAttachments() {
        return attachmentService.getAllAttachments();
    }

    @GetMapping("/ticket/{ticketId}")
    public Response getAttachmentsByTicket(@PathVariable Long ticketId) {
        return attachmentService.getAttachmentsByTicket(ticketId);
    }

    @GetMapping("/user/{userId}")
    public Response getAttachmentsByUser(@PathVariable Long userId) {
        return attachmentService.getAttachmentsByUser(userId);
    }

    @DeleteMapping("/{id}")
    public Response deleteAttachment(@PathVariable Long id) {
        return attachmentService.deleteAttachment(id);
    }
}