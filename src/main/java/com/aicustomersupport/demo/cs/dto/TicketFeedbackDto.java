package com.aicustomersupport.demo.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketFeedbackDto {

    private Long id;

    private Long ticketId;

    private Long customerId;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}