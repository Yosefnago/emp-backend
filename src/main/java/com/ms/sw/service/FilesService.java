package com.ms.sw.service;

import com.ms.sw.Dto.FileDto;
import com.ms.sw.entity.Documents;
import com.ms.sw.entity.Employees;
import com.ms.sw.exception.document.DocumentNotFoundException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.repository.EmployeeRepository;
import com.ms.sw.repository.FilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FilesService {

    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads");

    private FilesRepository filesRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public FilesService(FilesRepository filesRepository, EmployeeRepository employeeRepository) {
        this.filesRepository = filesRepository;
        this.employeeRepository = employeeRepository;
    }


    public List<FileDto> getAllFiles(String personalId, String ownerUsername) {

        log.info("getAllFiles personalId = {} by user {}", personalId, ownerUsername);

        List<Documents> list = filesRepository.getAllFilesByPersonalIdAndOwner(personalId, ownerUsername);

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

    public Documents saveFile(MultipartFile file, String personalId, String ownerUsername) {

        log.info("File upload attempt for file: {}", file.getOriginalFilename());
        try {
            Files.createDirectories(UPLOAD_DIR);

            Employees emp = employeeRepository.findByPersonalIdAndOwner(personalId, ownerUsername)
                    .orElseThrow(() -> new EmployeesNotFoundException("Employee not found or unauthorized: " + personalId));

            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + extension;
            Path filePath = UPLOAD_DIR.resolve(uniqueFileName);

            file.transferTo(filePath);

            Documents doc = new Documents();
            doc.setFilePath(filePath.toString());
            doc.setUploadedAt(LocalDateTime.now());
            doc.setEmployee(emp);

            return filesRepository.save(doc);

        } catch (EmployeesNotFoundException e) {
            throw e;
        } catch (IOException e) {
            log.error("File upload failed for personalId {}: {}", personalId, e.getMessage(), e);
            throw new RuntimeException("File storage failure: unable to write file to disk.", e);
        }catch (Exception e) {
            log.error("Unknown file upload failure for personalId {}: {}", personalId, e.getMessage(), e);
            throw new RuntimeException("Unknown file upload failure.", e);
        }
    }
    @Transactional
    public void deleteFile(Long documentId, String ownerUsername) {

        log.info("File delete attempt for ID: {} by user {}", documentId, ownerUsername);

        int deletedCount = filesRepository.deleteByIdAndOwner(documentId, ownerUsername);

        if (deletedCount == 0) {
            log.warn("Deletion failed: Document ID {} not found or unauthorized.", documentId);
            throw new DocumentNotFoundException("Document not found or unauthorized to delete.");
        }
    }
    public Documents getSecureDocument(Long id, String ownerUsername) {
        return filesRepository.findByIdAndOwner(id, ownerUsername)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found or unauthorized: " + id));
    }

    public ResponseEntity<byte[]> buildInlineFileResponse(Long id, String ownerUsername) {

        Documents doc = getSecureDocument(id, ownerUsername);

        String fileName = extractFileName(doc.getFilePath());
        Path filePath = Path.of(doc.getFilePath());

        byte[] bytes;

        try {
            bytes = Files.readAllBytes(filePath);
        } catch (NoSuchFileException e) {
            log.error("File not found on disk: {}", doc.getFilePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            log.error("Error reading file content from disk: {}", doc.getFilePath(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String mimeType;
        try {
            mimeType = Files.probeContentType(filePath);
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));

        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        headers.setContentDisposition(contentDisposition);
        headers.add("X-Content-Type-Options", "nosniff");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .body(bytes);
    }

}
