package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {

    List<TicketAttachment> findByTicketId(Long ticketId);

    List<TicketAttachment> findByUploadedById(Long userId);
}