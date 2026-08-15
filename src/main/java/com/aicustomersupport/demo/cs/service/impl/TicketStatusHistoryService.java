package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketStatusHistoryDto;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.repository.TicketStatusHistoryRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketStatusHistoryService implements ITicketStatusHistoryService {

    @Autowired
    private TicketStatusHistoryRepository historyRepository;

    @Override
    public Response createStatusHistory(TicketStatusHistory statusHistory) {
        try {
            TicketStatusHistory savedHistory = historyRepository.save(statusHistory);
            return Response.builder()
                    .statusCode(200)
                    .message("Ticket status history created successfully")
                    .ticketStatusHistory(convertToDto(savedHistory))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error creating status history: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getStatusHistory(Long id) {
        try {
            Optional<TicketStatusHistory> historyOpt = historyRepository.findById(id);
            if (historyOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Status history retrieved successfully")
                        .ticketStatusHistory(convertToDto(historyOpt.get()))
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("Status history not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving status history: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getAllStatusHistory() {
        try {
            List<TicketStatusHistory> histories = historyRepository.findAll();
            List<TicketStatusHistoryDto> historyDtos = histories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("All status histories retrieved successfully")
                    .ticketStatusHistories(historyDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving status histories: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getStatusHistoryByTicket(Long ticketId) {
        try {
            List<TicketStatusHistory> histories = historyRepository.findByTicketId(ticketId);
            List<TicketStatusHistoryDto> historyDtos = histories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("Ticket status history retrieved successfully")
                    .ticketStatusHistories(historyDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving ticket status history: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getStatusHistoryByUser(Long userId) {
        try {
            List<TicketStatusHistory> histories = historyRepository.findByChangedById(userId);
            List<TicketStatusHistoryDto> historyDtos = histories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("User status history retrieved successfully")
                    .ticketStatusHistories(historyDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving user status history: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteStatusHistory(Long id) {
        try {
            if (historyRepository.existsById(id)) {
                historyRepository.deleteById(id);
                return Response.builder()
                        .statusCode(200)
                        .message("Status history deleted successfully")
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("Status history not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error deleting status history: " + e.getMessage())
                    .build();
        }
    }

    // Helper method to convert Entity -> DTO safely
    private TicketStatusHistoryDto convertToDto(TicketStatusHistory history) {
        return TicketStatusHistoryDto.builder()
                .id(history.getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .changedAt(history.getChangedAt())
                .ticketId(history.getTicket() != null ? history.getTicket().getId() : null)
                .changedByUserId(history.getChangedBy() != null ? history.getChangedBy().getId() : null)
                .build();
    }
}