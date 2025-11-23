package com.ms.sw.service;

import com.ms.sw.Dto.FileDto;
import com.ms.sw.entity.Documents;
import com.ms.sw.entity.Employees;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.FilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FilesService {

    private FilesRepository filesRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public FilesService(FilesRepository filesRepository, EmployeeRepository employeeRepository) {
        this.filesRepository = filesRepository;
        this.employeeRepository = employeeRepository;
    }


    public List<FileDto> getAllFiles(String personalId) {

        log.info("getAllFiles personalId = {}", personalId);

        List<Documents> list = filesRepository.getAllFilesByPersonalId(personalId);

        return list.stream()
                .map(doc -> new FileDto(
                        doc.getId(),
                        extractFileName(doc.getFilePath()),
                        doc.getFilePath(),
                        doc.getUploadedAt(),
                        doc.getEmployee().getPersonalId()
                ))
                .toList();
    }
    public String extractFileName(String filePath) {
        return filePath.substring(filePath.replace("\\", "/").lastIndexOf("/") + 1);
    }
    public Documents  saveFile(MultipartFile file, String personalId) {
        log.info("File attempt = " + file.getOriginalFilename());
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            Files.createDirectories(Path.of(uploadDir));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Path.of(uploadDir + fileName);

            file.transferTo(filePath.toFile());

            Employees emp = employeeRepository.findByPersonalId(personalId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + personalId));



            Documents doc = new Documents();
            doc.setFilePath(filePath.toString());
            doc.setUploadedAt(LocalDateTime.now());
            doc.setEmployee(emp);

            return filesRepository.save(doc);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
    public void deleteFile(String fileName) {
        log.info("File attempt = " + fileName);
        filesRepository.deleteByFileName(fileName);
    }
    /**
     * Retrieves the minimal file metadata (path and name) from the database.
     *
     * @param id The document ID.
     * @return FileDto containing path and name, or null if not found.
     */
    public FileDto getFileMetadata(Long id) {
        // Assuming filesRepository.findById(id) returns an Optional<Documents>
        // and we map it to a FileDto containing the necessary path and name.
        return filesRepository.findById(id).map(doc -> new FileDto(
                doc.getId(),
                extractFileName(doc.getFilePath()),
                doc.getFilePath(),
                doc.getUploadedAt(),
                doc.getEmployee().getPersonalId()
        )).orElse(null);
    }

    /**
     * Builds the ResponseEntity containing the file content for in-line display.
     * Uses Files.readAllBytes() for simple, efficient, and auto-closing resource management.
     *
     * @param id The document ID.
     * @return ResponseEntity with file data and HTTP headers.
     */
    public ResponseEntity<byte[]> buildInlineFileResponse(Long id) {
        FileDto dto = getFileMetadata(id);

        if (dto == null) {
            log.warn("File metadata not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path filePath = Path.of(dto.path());
        byte[] bytes;

        try {
            // Read all bytes from the file. This is simple and highly efficient for files up to a few MBs.
            bytes = Files.readAllBytes(filePath);
        } catch (NoSuchFileException e) {
            log.error("File not found on disk: {}", dto.path());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            log.error("Error reading file content from disk: {}", dto.path(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Determine MIME Type
        String mimeType;
        try {
            mimeType = Files.probeContentType(filePath);
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default to binary stream
        }
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // Build HTTP Headers
        HttpHeaders headers = new HttpHeaders();

        // 1. Set Content-Type
        headers.setContentType(MediaType.parseMediaType(mimeType));

        // 2. Set Content-Disposition for 'inline' display with correct UTF-8 encoding for filename
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename(dto.name(), StandardCharsets.UTF_8)
                .build();
        headers.setContentDisposition(contentDisposition);

        // 3. Recommended security header
        headers.add("X-Content-Type-Options", "nosniff");

        // Return the file content
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .body(bytes);
    }
    public ResponseEntity<byte[]> buildDownloadResponse(String relativePath) {

        Path filePath = Path.of(System.getProperty("user.dir"), "uploads", relativePath);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(filePath);
        } catch (IOException e) {
            mimeType = "application/octet-stream";
        }

        String fileName = filePath.getFileName().toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType(mimeType))
                .body(bytes);
    }


}
