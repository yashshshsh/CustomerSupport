package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.Response;

public interface ITicketStatusHistoryService {

    Response getStatusHistory(Long id);

    Response getAllStatusHistory();

    Response getStatusHistoryByTicket(Long ticketId);

    Response getStatusHistoryByUser(Long userId);
}