package com.aicustomersupport.demo.cs.repository;

import com.aicustomersupport.demo.cs.model.TicketFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketFeedbackRepository
        extends JpaRepository<TicketFeedback, Long> {

    Optional<TicketFeedback> findByTicketId(Long ticketId);

    boolean existsByTicketId(Long ticketId);
}