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
public class TicketAttachmentDto {

    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private Long ticketId;
    private Long uploadedById;
    private LocalDateTime createdAt;
}