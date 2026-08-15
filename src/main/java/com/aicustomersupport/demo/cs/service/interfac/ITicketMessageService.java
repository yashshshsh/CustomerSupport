package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketMessage;

public interface ITicketMessageService {

    Response createMessage(TicketMessage ticketMessage);

    Response getMessage(Long id);

    Response getAllMessages();

    Response getMessagesByTicket(Long ticketId);

    Response getMessagesBySender(Long senderId);

    Response updateMessage(TicketMessage ticketMessage, Long id);

    Response deleteMessage(Long id);
}