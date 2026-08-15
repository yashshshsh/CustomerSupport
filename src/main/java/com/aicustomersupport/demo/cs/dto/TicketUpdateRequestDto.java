package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.TicketPriority;
import com.aicustomersupport.demo.cs.model.TicketStatus;

public class TicketUpdateRequestDto {

    private String subject;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private Long customerId;

    private Long assignedAgentId;

    private Long categoryId;

    private Long changedByUserId;

    public TicketUpdateRequestDto() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAssignedAgentId() {
        return assignedAgentId;
    }

    public void setAssignedAgentId(Long assignedAgentId) {
        this.assignedAgentId = assignedAgentId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(Long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }
}