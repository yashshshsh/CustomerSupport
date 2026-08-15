package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketAttachmentRepository
        extends JpaRepository<TicketAttachment, Long> {

    List<TicketAttachment> findByTicketId(Long ticketId);

    List<TicketAttachment> findByUploadedById(Long userId);
}