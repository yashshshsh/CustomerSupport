package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketMessageDto;
import com.aicustomersupport.demo.cs.model.Role;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketMessage;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.TicketMessageRepository;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
import com.aicustomersupport.demo.cs.repository.UserRepository;
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

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response createMessage(TicketMessage ticketMessage) {

        Response response = new Response();

        try {

            if (ticketMessage == null) {
                response.setStatusCode(400);
                response.setMessage("Ticket message is required");
                return response;
            }

            if (ticketMessage.getMessage() == null ||
                    ticketMessage.getMessage().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("Message cannot be empty");
                return response;
            }

            if (ticketMessage.getTicket() == null ||
                    ticketMessage.getTicket().getId() == null) {

                response.setStatusCode(400);
                response.setMessage("Ticket is required");
                return response;
            }

            if (ticketMessage.getSender() == null ||
                    ticketMessage.getSender().getId() == null) {

                response.setStatusCode(400);
                response.setMessage("Sender is required");
                return response;
            }

            Long ticketId =
                    ticketMessage.getTicket().getId();

            Long senderId =
                    ticketMessage.getSender().getId();

            Optional<Ticket> ticketOptional =
                    ticketRepository.findById(ticketId);

            if (ticketOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage(
                        "Ticket not found with id: " + ticketId
                );
                return response;
            }

            Optional<User> senderOptional =
                    userRepository.findById(senderId);

            if (senderOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage(
                        "Sender not found with id: " + senderId
                );
                return response;
            }

            Ticket ticket = ticketOptional.get();
            User sender = senderOptional.get();

            if (sender.getRole() == null) {

                response.setStatusCode(403);
                response.setMessage(
                        "Sender does not have an assigned role"
                );
                return response;
            }

            boolean authorized = false;

            if (sender.getRole() == Role.ADMIN) {

                authorized = true;

            } else if (sender.getRole() == Role.CUSTOMER) {

                authorized =
                        ticket.getCustomer() != null &&
                                ticket.getCustomer().getId() != null &&
                                ticket.getCustomer().getId().equals(senderId);

            } else if (sender.getRole() == Role.AGENT) {

                authorized =
                        ticket.getAssignedAgent() != null &&
                                ticket.getAssignedAgent().getId() != null &&
                                ticket.getAssignedAgent().getId().equals(senderId);
            }

            if (!authorized) {

                response.setStatusCode(403);
                response.setMessage(
                        "Sender is not authorized to post messages on this ticket"
                );
                return response;
            }

            ticketMessage.setTicket(ticket);
            ticketMessage.setSender(sender);
            ticketMessage.setMessage(
                    ticketMessage.getMessage().trim()
            );

            TicketMessage savedMessage =
                    ticketMessageRepository.save(ticketMessage);

            response.setStatusCode(201);
            response.setMessage(
                    "Ticket message created successfully"
            );
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
                response.setMessage(
                        "Ticket message not found"
                );

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

            if (!ticketRepository.existsById(ticketId)) {

                response.setStatusCode(404);
                response.setMessage(
                        "Ticket not found with id: " + ticketId
                );

                return response;
            }

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

            if (!userRepository.existsById(senderId)) {

                response.setStatusCode(404);
                response.setMessage(
                        "Sender not found with id: " + senderId
                );

                return response;
            }

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

            if (ticketMessage == null) {

                response.setStatusCode(400);
                response.setMessage(
                        "Ticket message is required"
                );

                return response;
            }

            Optional<TicketMessage> messageOptional =
                    ticketMessageRepository.findById(id);

            if (messageOptional.isEmpty()) {

                response.setStatusCode(404);
                response.setMessage(
                        "Ticket message not found"
                );

                return response;
            }

            TicketMessage existingMessage =
                    messageOptional.get();

            if (ticketMessage.getMessage() != null) {

                if (ticketMessage.getMessage().isBlank()) {

                    response.setStatusCode(400);
                    response.setMessage(
                            "Message cannot be empty"
                    );

                    return response;
                }

                existingMessage.setMessage(
                        ticketMessage.getMessage().trim()
                );
            }

            TicketMessage updatedMessage =
                    ticketMessageRepository.save(
                            existingMessage
                    );

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
                response.setMessage(
                        "Ticket message not found"
                );

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