package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketAttachment;

public interface ITicketAttachmentService {

    Response createAttachment(TicketAttachment attachment);

    Response getAttachment(Long id);

    Response getAllAttachments();

    Response getAttachmentsByTicket(Long ticketId);

    Response getAttachmentsByUser(Long userId);

    Response updateAttachment(TicketAttachment attachment, Long id);

    Response deleteAttachment(Long id);
}