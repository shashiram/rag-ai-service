package com.rag.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for querying the RAG system")
public class QueryRequest {
    
    @Schema(
        description = "The question to ask the RAG system",
        example = "What is the main topic of the document?",
        required = true
    )
    private String question;
    
    public QueryRequest() {}
    
    public QueryRequest(String question) {
        this.question = question;
    }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
}