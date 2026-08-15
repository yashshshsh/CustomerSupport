package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomerId(Long customerId);

    List<Ticket> findByAssignedAgentId(Long agentId);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByCategoryId(Long categoryId);
}