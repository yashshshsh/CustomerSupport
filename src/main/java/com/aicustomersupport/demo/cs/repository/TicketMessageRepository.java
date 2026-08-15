package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.TicketMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketMessageRepository
        extends JpaRepository<TicketMessage, Long> {

    List<TicketMessage> findByTicketId(Long ticketId);

    List<TicketMessage> findBySenderId(Long senderId);
}