package com.aicustomersupport.demo.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketMessageDto {

    private Long id;

    private Long ticketId;

    private Long senderId;

    private String message;

    private LocalDateTime createdAt;

}