package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketStatusHistoryRepository
        extends JpaRepository<TicketStatusHistory, Long> {

    List<TicketStatusHistory> findByTicketId(Long ticketId);

    List<TicketStatusHistory> findByChangedById(Long userId);
}