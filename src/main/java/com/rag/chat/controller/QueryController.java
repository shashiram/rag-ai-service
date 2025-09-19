package com.rag.chat.controller;

import com.rag.chat.dto.QueryRequest;
import com.rag.chat.dto.QueryResponse;
import com.rag.chat.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
@Tag(name = "Query", description = "APIs for querying the RAG system")
public class QueryController {
    
    private final RagService ragService;
    
    public QueryController(RagService ragService) {
        this.ragService = ragService;
    }
    
    @Operation(
        summary = "Query the RAG system",
        description = "Ask a question and get an answer based on the uploaded documents"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Successful response",
            content = @Content(schema = @Schema(implementation = QueryResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = QueryResponse.class))
        )
    })
    @PostMapping

    public ResponseEntity<QueryResponse> query(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Query request containing the question",
                required = true,
                content = @Content(schema = @Schema(implementation = QueryRequest.class))
            )
            @RequestBody QueryRequest request) {
        try {
            String response = ragService.generateResponse(request.getQuestion());
            return ResponseEntity.ok(new QueryResponse(response, "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new QueryResponse(null, "Error: " + e.getMessage()));
        }
    }
}