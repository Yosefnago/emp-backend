package com.ms.sw.controller;

import com.ms.sw.Dto.FileDto;
import com.ms.sw.customUtils.CurrentUser;
import com.ms.sw.entity.Documents;
import com.ms.sw.entity.User;
import com.ms.sw.service.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Files", description = "files controllers")
@RestController
@RequestMapping("/files")
@Slf4j
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @Operation(summary = "Get files for employee by ID")
    @GetMapping("/{personalId}")
    public ResponseEntity<List<FileDto>> getFilesForEmployee(@CurrentUser User user, @PathVariable String personalId) {

        log.info("Get files for employee {} by user: {}", personalId, user.getUsername());
        return ResponseEntity.ok(filesService.getAllFiles(personalId, user.getUsername()));
    }

    @PostMapping("/upload/{personalId}")
    public ResponseEntity<?> uploadFile(@CurrentUser User user, @RequestParam("file") List<MultipartFile> files, @PathVariable String personalId) {

        log.info("File upload request for employee {} by user: {}", personalId, user.getUsername());

        List<Documents> savedDocs = new ArrayList<>();

        for (MultipartFile file : files) {
            Documents saved = filesService.saveFile(file, personalId, user.getUsername());
            savedDocs.add(saved);
        }

        List<FileDto> dtos = savedDocs.stream()
                .map(saved -> new FileDto(
                        saved.getId(),
                        filesService.extractFileName(saved.getFilePath()),
                        saved.getFilePath(),
                        saved.getUploadedAt(),
                        saved.getEmployee().getPersonalId()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }
    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<Void> deleteFile(@CurrentUser User user, @PathVariable Long documentId) {

        log.info("Delete file ID {} requested by user: {}", documentId, user.getUsername());

        filesService.deleteFile(documentId, user.getUsername());
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Show file content in-line (e.g., in a browser tab)")
    @GetMapping("/show/{id}")
    public ResponseEntity<byte[]> show(@CurrentUser User user, @PathVariable Long id) {
        log.info("Serving file request for ID: {} requested by user: {}", id, user.getUsername());
        return filesService.buildInlineFileResponse(id, user.getUsername());
    }
    @Operation(summary = "Download file content")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@CurrentUser User user, @PathVariable Long id) {

        log.info("Download request for ID: {} requested by user: {}", id, user.getUsername());

        ResponseEntity<byte[]> inlineResponse = filesService.buildInlineFileResponse(id, user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(inlineResponse.getHeaders());

        headers.setContentDisposition(
                ContentDisposition.builder("attachment") // Change from 'inline' to 'attachment'
                        .filename(headers.getContentDisposition().getFilename())
                        .build()
        );

        headers.remove("X-Content-Type-Options");

        return ResponseEntity.status(inlineResponse.getStatusCode())
                .headers(headers)
                .body(inlineResponse.getBody());
    }

}
