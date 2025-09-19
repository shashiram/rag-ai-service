package com.rag.chat.controller;

import com.rag.chat.service.FileProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/files")
@Tag(name = "File Upload", description = "APIs for uploading and processing documents")
public class FileUploadController {
    
    private final FileProcessingService fileProcessingService;
    
    public FileUploadController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }
    
    @Operation(
        summary = "Upload a text, PDF or Doc file",
        description = "Upload a text file to be processed and stored in the vector database"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "File processed successfully",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file format or empty file",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping(value = "/upload", consumes = "multipart/form-data")

    public ResponseEntity<String> uploadFile(
            @Parameter(description = "Text file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            fileProcessingService.processTextFile(file);
            return ResponseEntity.ok("File processed successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing file: " + e.getMessage());
        }
    }
}