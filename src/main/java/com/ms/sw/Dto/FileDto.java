package com.ms.sw.Dto;

import java.time.LocalDateTime;

/**
 * {@code FileDto} is an immutable Data Transfer Object (DTO)
 * used to represent the essential metadata of a file or document stored on the server,
 * typically associated with a specific employee.
 * <p>It provides the necessary information for the client application (Angular) to
 * list, track, and initiate actions (such as download, view, or delete) on a document
 * without exposing sensitive file system details.
 * @param id The unique database identifier for the document record.
 * @param name The user-friendly name of the file (e.g., "Contract_2025.pdf").
 * @param path The relative or absolute path where the file is physically stored on the server/storage.
 * @param uploadedAt The timestamp indicating when the document was initially uploaded.
 * @param personalId The personal identifier of the employee to whom this file belongs.
 * @see com.ms.sw.controller.FilesController
 * @see com.ms.sw.service.FilesService
 */
public record FileDto(
        Long id,
        String name,
        String path,
        LocalDateTime uploadedAt,
        String personalId
) {}