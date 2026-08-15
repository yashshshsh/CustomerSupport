package com.aicustomersupport.demo.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeArticleDto {

    private Long id;

    private String title;

    private String content;

    private Long categoryId;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}