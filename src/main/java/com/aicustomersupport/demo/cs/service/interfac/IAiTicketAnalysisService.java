package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.AiTicketAnalysisDto;

public interface IAiTicketAnalysisService {

    AiTicketAnalysisDto analyzeTicket(
            String ticketText
    );
}