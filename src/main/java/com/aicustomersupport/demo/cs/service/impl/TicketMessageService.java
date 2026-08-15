package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketMessage;
import com.aicustomersupport.demo.cs.repository.TicketMessageRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            response.setMessage("Message created successfully");
            response.setTicketMessage(savedMessage);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating message: " + e.getMessage()
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

                response.setStatusCode(400);
                response.setMessage("Message not found");

                return response;
            }

            response.setStatusCode(200);
            response.setTicketMessage(messageOptional.get());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting message: " + e.getMessage()
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

            response.setStatusCode(200);
            response.setMessage("Messages retrieved successfully");
            response.setTicketMessages(messages);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting messages: " + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getMessagesByTicket(Long ticketId) {

        Response response = new Response();

        try {

            List<TicketMessage> messages =
                    ticketMessageRepository.findByTicketId(ticketId);

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket messages retrieved successfully"
            );
            response.setTicketMessages(messages);

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
                    ticketMessageRepository.findBySenderId(senderId);

            response.setStatusCode(200);
            response.setMessage(
                    "Sender messages retrieved successfully"
            );
            response.setTicketMessages(messages);

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

                response.setStatusCode(400);
                response.setMessage("Message not found");

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
            response.setMessage("Message updated successfully");
            response.setTicketMessage(updatedMessage);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating message: "
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

                response.setStatusCode(400);
                response.setMessage("Message not found");

                return response;
            }

            ticketMessageRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("Message deleted successfully");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting message: " + e.getMessage()
            );
        }

        return response;
    }
}