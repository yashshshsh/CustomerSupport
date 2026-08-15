package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;

public interface ITicketStatusHistoryService {

    Response createStatusHistory(TicketStatusHistory statusHistory);

    Response getStatusHistory(Long id);

    Response getAllStatusHistory();

    Response getStatusHistoryByTicket(Long ticketId);

    Response getStatusHistoryByUser(Long userId);

    Response deleteStatusHistory(Long id);
}