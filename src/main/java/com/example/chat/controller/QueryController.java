package com.example.chat.controller;

import com.example.chat.dto.QueryRequest;
import com.example.chat.dto.QueryResponse;
import com.example.chat.service.RagService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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