package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketMessageDto;
import com.aicustomersupport.demo.cs.model.TicketMessage;
import com.aicustomersupport.demo.cs.repository.TicketMessageRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketMessageService implements ITicketMessageService {

    @Autowired
    private TicketMessageRepository ticketMessageRepository;

    @Override
    public Response createMessage(TicketMessage ticketMessage) {

        Response response = new Response();

        try {

            TicketMessage savedMessage =
                    ticketMessageRepository.save(ticketMessage);

            response.setStatusCode(200);
            response.setMessage("Ticket message created successfully");
            response.setTicketMessage(
                    convertToDto(savedMessage)
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating ticket message: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getMessage(Long id) {

        Response response = new Response();

        try {

            Optional<TicketMessage> messageOptional =
                    ticketMessageRepository.findById(id);

            if (messageOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Ticket message not found");

                return response;
            }

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket message retrieved successfully"
            );
            response.setTicketMessage(
                    convertToDto(messageOptional.get())
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket message: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllMessages() {

        Response response = new Response();

        try {

            List<TicketMessage> messages =
                    ticketMessageRepository.findAll();

            List<TicketMessageDto> messageDtos =
                    messages.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket messages retrieved successfully"
            );
            response.setTicketMessages(messageDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket messages: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getMessagesByTicket(Long ticketId) {

        Response response = new Response();

        try {

            List<TicketMessage> messages =
                    ticketMessageRepository
                            .findByTicketId(ticketId);

            List<TicketMessageDto> messageDtos =
                    messages.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket messages retrieved successfully"
            );
            response.setTicketMessages(messageDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket messages: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getMessagesBySender(Long senderId) {

        Response response = new Response();

        try {

            List<TicketMessage> messages =
                    ticketMessageRepository
                            .findBySenderId(senderId);

            List<TicketMessageDto> messageDtos =
                    messages.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(
                    "Sender messages retrieved successfully"
            );
            response.setTicketMessages(messageDtos);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting sender messages: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateMessage(
            TicketMessage ticketMessage,
            Long id) {

        Response response = new Response();

        try {

            Optional<TicketMessage> messageOptional =
                    ticketMessageRepository.findById(id);

            if (messageOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage("Ticket message not found");

                return response;
            }

            TicketMessage existingMessage =
                    messageOptional.get();

            if (ticketMessage.getMessage() != null) {
                existingMessage.setMessage(
                        ticketMessage.getMessage()
                );
            }

            TicketMessage updatedMessage =
                    ticketMessageRepository.save(existingMessage);

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket message updated successfully"
            );
            response.setTicketMessage(
                    convertToDto(updatedMessage)
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating ticket message: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteMessage(Long id) {

        Response response = new Response();

        try {

            if (!ticketMessageRepository.existsById(id)) {

                response.setStatusCode(404);
                response.setMessage("Ticket message not found");

                return response;
            }

            ticketMessageRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket message deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting ticket message: "
                            + e.getMessage()
            );
        }

        return response;
    }

    private TicketMessageDto convertToDto(
            TicketMessage ticketMessage) {

        return new TicketMessageDto(
                ticketMessage.getId(),
                ticketMessage.getTicket() != null
                        ? ticketMessage.getTicket().getId()
                        : null,
                ticketMessage.getSender() != null
                        ? ticketMessage.getSender().getId()
                        : null,
                ticketMessage.getMessage(),
                ticketMessage.getCreatedAt()
        );
    }
}