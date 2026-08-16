package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.AiDecisionDto;

public interface IAiDecisionService {

    AiDecisionDto makeDecision(
            String ticketText
    );
}