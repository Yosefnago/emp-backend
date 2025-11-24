package com.ms.sw.controller;

import com.ms.sw.Dto.FileDto;
import com.ms.sw.entity.Documents;
import com.ms.sw.service.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "get files for employee by id")
    @GetMapping("/{personalId}")
    public ResponseEntity<List<FileDto>> getFilesForEmployee(@PathVariable String personalId) {

        log.info("get files for employee by id: {}", personalId);
        return ResponseEntity.ok(filesService.getAllFiles(personalId));
    }

    @PostMapping("/upload/{personalId}")
    public ResponseEntity<?> uploadFile(@RequestParam("file")  List<MultipartFile> files, @PathVariable String personalId) {
        log.info("file upload request: {}");

        List<Documents> savedDocs = new ArrayList<>();

        for (MultipartFile file : files) {
            Documents saved = filesService.saveFile(file, personalId);
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
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        log.info("delete file: {}", fileName);

        filesService.deleteFile(fileName);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Show file content in-line (e.g., in a browser tab)")
    @GetMapping("/show/{id}") // Relative path: /files/show/{id}
    public ResponseEntity<byte[]> show(@PathVariable Long id)  {
        log.info("Serving file request for ID: {}", id);
        return filesService.buildInlineFileResponse(id);
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        log.info("Downloading file from fileName: {}", fileName);
        return filesService.buildDownloadResponse(fileName);
    }

}
