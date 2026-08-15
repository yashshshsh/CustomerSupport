package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ITicketAttachmentService {

    Response uploadAttachment(MultipartFile file, Long ticketId, Long uploadedById);

    Response getAttachment(Long id);

    Response getAllAttachments();

    Response getAttachmentsByTicket(Long ticketId);

    Response getAttachmentsByUser(Long userId);

    Response deleteAttachment(Long id);
}