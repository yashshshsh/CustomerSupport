package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.dto.TicketAttachmentDto;
import com.aicustomersupport.demo.cs.model.Ticket;
import com.aicustomersupport.demo.cs.model.TicketAttachment;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.TicketAttachmentRepository;
import com.aicustomersupport.demo.cs.repository.TicketRepository;
import com.aicustomersupport.demo.cs.repository.UserRepository;
import com.aicustomersupport.demo.cs.service.interfac.ITicketAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketAttachmentService implements ITicketAttachmentService {

    @Autowired
    private TicketAttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIR = "uploads/";

    @Override
    public Response uploadAttachment(MultipartFile file, Long ticketId, Long uploadedById) {
        try {
            if (file.isEmpty()) {
                return Response.builder().statusCode(400).message("Uploaded file is empty").build();
            }

            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            if (ticketOpt.isEmpty()) {
                return Response.builder().statusCode(404).message("Ticket not found with id: " + ticketId).build();
            }

            Optional<User> userOpt = userRepository.findById(uploadedById);
            if (userOpt.isEmpty()) {
                return Response.builder().statusCode(404).message("User not found with id: " + uploadedById).build();
            }

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            TicketAttachment attachment = TicketAttachment.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(filePath.toString())
                    .ticket(ticketOpt.get())
                    .uploadedBy(userOpt.get())
                    .build();

            TicketAttachment savedAttachment = attachmentRepository.save(attachment);

            return Response.builder()
                    .statusCode(200)
                    .message("File uploaded successfully")
                    .ticketAttachment(convertToDto(savedAttachment))
                    .build();

        } catch (IOException e) {
            return Response.builder().statusCode(500).message("Failed to save file locally: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error uploading attachment: " + e.getMessage()).build();
        }
    }

    @Override
    public Response getAttachment(Long id) {
        try {
            Optional<TicketAttachment> attachmentOpt = attachmentRepository.findById(id);
            if (attachmentOpt.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("Attachment retrieved successfully")
                        .ticketAttachment(convertToDto(attachmentOpt.get()))
                        .build();
            }
            return Response.builder().statusCode(404).message("Attachment not found with id: " + id).build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error retrieving attachment: " + e.getMessage()).build();
        }
    }

    @Override
    public Response getAllAttachments() {
        try {
            List<TicketAttachment> attachments = attachmentRepository.findAll();
            List<TicketAttachmentDto> attachmentDtos = attachments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("Attachments retrieved successfully")
                    .ticketAttachments(attachmentDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error retrieving attachments: " + e.getMessage()).build();
        }
    }

    @Override
    public Response getAttachmentsByTicket(Long ticketId) {
        try {
            List<TicketAttachment> attachments = attachmentRepository.findByTicketId(ticketId);
            List<TicketAttachmentDto> attachmentDtos = attachments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("Ticket attachments retrieved successfully")
                    .ticketAttachments(attachmentDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error retrieving ticket attachments: " + e.getMessage()).build();
        }
    }

    @Override
    public Response getAttachmentsByUser(Long userId) {
        try {
            List<TicketAttachment> attachments = attachmentRepository.findByUploadedById(userId);
            List<TicketAttachmentDto> attachmentDtos = attachments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message("User attachments retrieved successfully")
                    .ticketAttachments(attachmentDtos)
                    .build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error retrieving user attachments: " + e.getMessage()).build();
        }
    }

    @Override
    public Response deleteAttachment(Long id) {
        try {
            Optional<TicketAttachment> attachmentOpt = attachmentRepository.findById(id);
            if (attachmentOpt.isPresent()) {
                TicketAttachment attachment = attachmentOpt.get();

                File file = new File(attachment.getFilePath());
                if (file.exists()) {
                    file.delete();
                }

                attachmentRepository.deleteById(id);
                return Response.builder().statusCode(200).message("Attachment and local file deleted successfully").build();
            }
            return Response.builder().statusCode(404).message("Attachment not found with id: " + id).build();
        } catch (Exception e) {
            return Response.builder().statusCode(500).message("Error deleting attachment: " + e.getMessage()).build();
        }
    }

    private TicketAttachmentDto convertToDto(TicketAttachment attachment) {
        return TicketAttachmentDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .filePath(attachment.getFilePath())
                .ticketId(attachment.getTicket() != null ? attachment.getTicket().getId() : null)
                .uploadedById(attachment.getUploadedBy() != null ? attachment.getUploadedBy().getId() : null)
                .createdAt(attachment.getCreatedAt())
                .build();
    }
}