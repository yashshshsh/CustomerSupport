package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.TicketPriority;
import com.aicustomersupport.demo.cs.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private Long id;

    private String subject;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private Long customerId;

    private Long assignedAgentId;

    private Long categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}