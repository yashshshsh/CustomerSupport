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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketAttachmentService implements ITicketAttachmentService {

    @Autowired
    private TicketAttachmentRepository attachmentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    @Override
    public Response uploadAttachment(
            MultipartFile file,
            Long ticketId,
            Long uploadedById) {

        try {

            if (file == null || file.isEmpty()) {
                return Response.builder()
                        .statusCode(400)
                        .message("Uploaded file is empty")
                        .build();
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                return Response.builder()
                        .statusCode(400)
                        .message("File size must not exceed 10 MB")
                        .build();
            }

            String contentType = file.getContentType();

            if (contentType == null ||
                    !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {

                return Response.builder()
                        .statusCode(400)
                        .message("File type is not allowed")
                        .build();
            }

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null ||
                    originalFileName.isBlank()) {

                return Response.builder()
                        .statusCode(400)
                        .message("File name is invalid")
                        .build();
            }

            String sanitizedFileName =
                    sanitizeFileName(originalFileName);

            if (sanitizedFileName.isBlank()) {
                return Response.builder()
                        .statusCode(400)
                        .message("File name is invalid")
                        .build();
            }

            Optional<Ticket> ticketOpt =
                    ticketRepository.findById(ticketId);

            if (ticketOpt.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message(
                                "Ticket not found with id: " + ticketId
                        )
                        .build();
            }

            Optional<User> userOpt =
                    userRepository.findById(uploadedById);

            if (userOpt.isEmpty()) {
                return Response.builder()
                        .statusCode(404)
                        .message(
                                "User not found with id: " + uploadedById
                        )
                        .build();
            }

            Path uploadPath =
                    Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

            Files.createDirectories(uploadPath);

            String storedFileName =
                    UUID.randomUUID() + "_" + sanitizedFileName;

            Path filePath =
                    uploadPath.resolve(storedFileName).normalize();

            if (!filePath.getParent().equals(uploadPath)) {
                return Response.builder()
                        .statusCode(400)
                        .message("Invalid file name")
                        .build();
            }

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            TicketAttachment attachment =
                    TicketAttachment.builder()
                            .fileName(sanitizedFileName)
                            .fileType(contentType)
                            .fileSize(file.getSize())
                            .filePath(filePath.toString())
                            .ticket(ticketOpt.get())
                            .uploadedBy(userOpt.get())
                            .build();

            TicketAttachment savedAttachment =
                    attachmentRepository.save(attachment);

            return Response.builder()
                    .statusCode(201)
                    .message("File uploaded successfully")
                    .ticketAttachment(
                            convertToDto(savedAttachment)
                    )
                    .build();

        } catch (IOException e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Failed to save file locally: "
                                    + e.getMessage()
                    )
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error uploading attachment: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getAttachment(Long id) {

        try {

            Optional<TicketAttachment> attachmentOpt =
                    attachmentRepository.findById(id);

            if (attachmentOpt.isPresent()) {

                return Response.builder()
                        .statusCode(200)
                        .message(
                                "Attachment retrieved successfully"
                        )
                        .ticketAttachment(
                                convertToDto(
                                        attachmentOpt.get()
                                )
                        )
                        .build();
            }

            return Response.builder()
                    .statusCode(404)
                    .message(
                            "Attachment not found with id: " + id
                    )
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error retrieving attachment: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getAllAttachments() {

        try {

            List<TicketAttachment> attachments =
                    attachmentRepository.findAll();

            List<TicketAttachmentDto> attachmentDtos =
                    attachments.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message(
                            "Attachments retrieved successfully"
                    )
                    .ticketAttachments(attachmentDtos)
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error retrieving attachments: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getAttachmentsByTicket(Long ticketId) {

        try {

            List<TicketAttachment> attachments =
                    attachmentRepository.findByTicketId(ticketId);

            List<TicketAttachmentDto> attachmentDtos =
                    attachments.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message(
                            "Ticket attachments retrieved successfully"
                    )
                    .ticketAttachments(attachmentDtos)
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error retrieving ticket attachments: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response getAttachmentsByUser(Long userId) {

        try {

            List<TicketAttachment> attachments =
                    attachmentRepository.findByUploadedById(userId);

            List<TicketAttachmentDto> attachmentDtos =
                    attachments.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList());

            return Response.builder()
                    .statusCode(200)
                    .message(
                            "User attachments retrieved successfully"
                    )
                    .ticketAttachments(attachmentDtos)
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error retrieving user attachments: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    @Override
    public Response deleteAttachment(Long id) {

        try {

            Optional<TicketAttachment> attachmentOpt =
                    attachmentRepository.findById(id);

            if (attachmentOpt.isEmpty()) {

                return Response.builder()
                        .statusCode(404)
                        .message(
                                "Attachment not found with id: " + id
                        )
                        .build();
            }

            TicketAttachment attachment =
                    attachmentOpt.get();

            File file =
                    new File(attachment.getFilePath());

            if (file.exists() && !file.delete()) {

                return Response.builder()
                        .statusCode(500)
                        .message(
                                "Failed to delete attachment file"
                        )
                        .build();
            }

            attachmentRepository.deleteById(id);

            return Response.builder()
                    .statusCode(200)
                    .message(
                            "Attachment and local file deleted successfully"
                    )
                    .build();

        } catch (Exception e) {

            return Response.builder()
                    .statusCode(500)
                    .message(
                            "Error deleting attachment: "
                                    + e.getMessage()
                    )
                    .build();
        }
    }

    private String sanitizeFileName(String fileName) {

        String cleanFileName =
                Paths.get(fileName)
                        .getFileName()
                        .toString();

        cleanFileName =
                cleanFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        cleanFileName =
                cleanFileName.replaceAll("\\.{2,}", ".");

        cleanFileName =
                cleanFileName.replaceAll("^\\.+", "");

        if (cleanFileName.length() > 150) {
            cleanFileName =
                    cleanFileName.substring(
                            cleanFileName.length() - 150
                    );
        }

        return cleanFileName;
    }

    private TicketAttachmentDto convertToDto(
            TicketAttachment attachment) {

        return TicketAttachmentDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .filePath(attachment.getFilePath())
                .ticketId(
                        attachment.getTicket() != null
                                ? attachment.getTicket().getId()
                                : null
                )
                .uploadedById(
                        attachment.getUploadedBy() != null
                                ? attachment.getUploadedBy().getId()
                                : null
                )
                .createdAt(attachment.getCreatedAt())
                .build();
    }
}