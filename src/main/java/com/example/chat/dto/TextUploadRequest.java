package com.example.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for uploading text content")
public class TextUploadRequest {
    
    @Schema(
        description = "The text content to process",
        example = "This is a sample document content about artificial intelligence...",
        required = true
    )
    private String content;
    
    @Schema(
        description = "Name for the document",
        example = "ai_introduction",
        required = true
    )
    private String documentName;
    
    public TextUploadRequest() {}
    
    public TextUploadRequest(String content, String documentName) {
        this.content = content;
        this.documentName = documentName;
    }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }
}