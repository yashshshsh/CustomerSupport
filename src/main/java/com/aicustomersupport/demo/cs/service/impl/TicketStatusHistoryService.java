package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketStatusHistory;
import com.aicustomersupport.demo.cs.repository.TicketStatusHistoryRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketStatusHistoryService
        implements ITicketStatusHistoryService {

    @Autowired
    private TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Override
    public Response createStatusHistory(
            TicketStatusHistory statusHistory) {

        Response response = new Response();

        try {

            TicketStatusHistory savedStatusHistory =
                    ticketStatusHistoryRepository.save(statusHistory);

            response.setStatusCode(200);
            response.setMessage("Status history created successfully");
            response.setTicketStatusHistory(savedStatusHistory);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating status history: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getStatusHistory(Long id) {

        Response response = new Response();

        try {

            Optional<TicketStatusHistory> statusHistoryOptional =
                    ticketStatusHistoryRepository.findById(id);

            if (statusHistoryOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Status history not found");

                return response;
            }

            response.setStatusCode(200);
            response.setTicketStatusHistory(
                    statusHistoryOptional.get()
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting status history: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllStatusHistory() {

        Response response = new Response();

        try {

            List<TicketStatusHistory> statusHistories =
                    ticketStatusHistoryRepository.findAll();

            response.setStatusCode(200);
            response.setMessage(
                    "Status histories retrieved successfully"
            );
            response.setTicketStatusHistories(statusHistories);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting status histories: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getStatusHistoryByTicket(Long ticketId) {

        Response response = new Response();

        try {

            List<TicketStatusHistory> statusHistories =
                    ticketStatusHistoryRepository
                            .findByTicketId(ticketId);

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket status history retrieved successfully"
            );
            response.setTicketStatusHistories(statusHistories);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket status history: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getStatusHistoryByUser(Long userId) {

        Response response = new Response();

        try {

            List<TicketStatusHistory> statusHistories =
                    ticketStatusHistoryRepository
                            .findByChangedById(userId);

            response.setStatusCode(200);
            response.setMessage(
                    "User status history retrieved successfully"
            );
            response.setTicketStatusHistories(statusHistories);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting user status history: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteStatusHistory(Long id) {

        Response response = new Response();

        try {

            if (!ticketStatusHistoryRepository.existsById(id)) {

                response.setStatusCode(400);
                response.setMessage("Status history not found");

                return response;
            }

            ticketStatusHistoryRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage(
                    "Status history deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting status history: "
                            + e.getMessage()
            );
        }

        return response;
    }
}