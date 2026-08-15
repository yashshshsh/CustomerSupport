package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.TicketAttachment;
import com.aicustomersupport.demo.cs.repository.TicketAttachmentRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketAttachmentService
        implements ITicketAttachmentService {

    @Autowired
    private TicketAttachmentRepository ticketAttachmentRepository;

    @Override
    public Response createAttachment(TicketAttachment attachment) {

        Response response = new Response();

        try {

            if (attachment.getTicket() == null ||
                    attachment.getTicket().getId() == null) {

                response.setStatusCode(400);
                response.setMessage("Ticket is required");

                return response;
            }

            if (attachment.getFileName() == null ||
                    attachment.getFileName().isBlank()) {

                response.setStatusCode(400);
                response.setMessage("File name is required");

                return response;
            }

            TicketAttachment savedAttachment =
                    ticketAttachmentRepository.save(attachment);

            response.setStatusCode(200);
            response.setMessage(
                    "Attachment created successfully"
            );
            response.setTicketAttachment(savedAttachment);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while creating attachment: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAttachment(Long id) {

        Response response = new Response();

        try {

            Optional<TicketAttachment> attachmentOptional =
                    ticketAttachmentRepository.findById(id);

            if (attachmentOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Attachment not found");

                return response;
            }

            response.setStatusCode(200);
            response.setTicketAttachment(
                    attachmentOptional.get()
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting attachment: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAllAttachments() {

        Response response = new Response();

        try {

            List<TicketAttachment> attachments =
                    ticketAttachmentRepository.findAll();

            response.setStatusCode(200);
            response.setMessage(
                    "Attachments retrieved successfully"
            );
            response.setTicketAttachments(attachments);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting attachments: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAttachmentsByTicket(Long ticketId) {

        Response response = new Response();

        try {

            List<TicketAttachment> attachments =
                    ticketAttachmentRepository
                            .findByTicketId(ticketId);

            response.setStatusCode(200);
            response.setMessage(
                    "Ticket attachments retrieved successfully"
            );
            response.setTicketAttachments(attachments);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting ticket attachments: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response getAttachmentsByUser(Long userId) {

        Response response = new Response();

        try {

            List<TicketAttachment> attachments =
                    ticketAttachmentRepository
                            .findByUploadedById(userId);

            response.setStatusCode(200);
            response.setMessage(
                    "User attachments retrieved successfully"
            );
            response.setTicketAttachments(attachments);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while getting user attachments: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response updateAttachment(
            TicketAttachment attachment,
            Long id) {

        Response response = new Response();

        try {

            Optional<TicketAttachment> attachmentOptional =
                    ticketAttachmentRepository.findById(id);

            if (attachmentOptional.isEmpty()) {

                response.setStatusCode(400);
                response.setMessage("Attachment not found");

                return response;
            }

            TicketAttachment existingAttachment =
                    attachmentOptional.get();

            if (attachment.getFileName() != null) {
                existingAttachment.setFileName(
                        attachment.getFileName()
                );
            }

            if (attachment.getFileType() != null) {
                existingAttachment.setFileType(
                        attachment.getFileType()
                );
            }

            if (attachment.getFileUrl() != null) {
                existingAttachment.setFileUrl(
                        attachment.getFileUrl()
                );
            }

            TicketAttachment updatedAttachment =
                    ticketAttachmentRepository.save(
                            existingAttachment
                    );

            response.setStatusCode(200);
            response.setMessage(
                    "Attachment updated successfully"
            );
            response.setTicketAttachment(updatedAttachment);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while updating attachment: "
                            + e.getMessage()
            );
        }

        return response;
    }

    @Override
    public Response deleteAttachment(Long id) {

        Response response = new Response();

        try {

            if (!ticketAttachmentRepository.existsById(id)) {

                response.setStatusCode(400);
                response.setMessage("Attachment not found");

                return response;
            }

            ticketAttachmentRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage(
                    "Attachment deleted successfully"
            );

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage(
                    "Error while deleting attachment: "
                            + e.getMessage()
            );
        }

        return response;
    }
}