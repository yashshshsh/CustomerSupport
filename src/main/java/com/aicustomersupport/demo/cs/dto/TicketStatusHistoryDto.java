package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatusHistoryDto {

    private Long id;

    private Long ticketId;

    private TicketStatus oldStatus;

    private TicketStatus newStatus;

    private Long changedByUserId;

    private LocalDateTime changedAt;
}