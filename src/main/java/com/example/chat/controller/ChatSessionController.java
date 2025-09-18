package com.example.chat.controller;


import com.example.chat.dto.ChatSessionDTO;
import com.example.chat.dto.CreateSessionRequest;
import com.example.chat.service.ChatSessionService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Chat Sessions", description = "APIs for managing chat sessions")
public class ChatSessionController {

    private static final Logger logger = LoggerFactory.getLogger(ChatSessionController.class);

    private final ChatSessionService chatSessionService;

    @Value("${rate-limiting.capacity:100}")
    private int rateLimitCapacity;

    @Value("${rate-limiting.refill-period-minutes:1}")
    private int rateLimitRefillPeriod;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping
    @Operation(summary = "Create a new chat session")
    public ResponseEntity<ChatSessionDTO> createSession(
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody CreateSessionRequest sessionRequest) {

        logger.info("Creating new Session");
        ChatSessionDTO chatSessionDTO = chatSessionService.createSession(sessionRequest);
        return new ResponseEntity<>(chatSessionDTO, HttpStatus.CREATED);
    }

//    @GetMapping
//    @Operation(summary = "Get all sessions for a user")
//    public ResponseEntity<List<ChatSessionDTO>> getSessions(
//            @RequestHeader("X-API-Key") String apiKey,
//            @RequestParam String userId) {
//        chatSessionService.getSessionsByUserId(userId);
//        return ResponseEntity.status(429).build();
//    }

//    @GetMapping("/paginated")
//    @Operation(summary = "Get paginated sessions for a user")
//    public ResponseEntity<Page<ChatSessionDTO>> getSessionsPaginated(
//            @RequestHeader("X-API-Key") String apiKey,
//            @RequestParam String userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//        Pageable pageable=PageRequest.of(page,size, Sort.by("createdAt").descending());
//        return ResponseEntity.status(429).build();
//    }

    //    @GetMapping("/favorites")
//    @Operation(summary = "Get favorite sessions for a user")
//    public ResponseEntity<List<ChatSessionDTO>> getFavoriteSessions(
//            @RequestHeader("X-API-Key") String apiKey,
//            @RequestParam String userId) {
//        chatSessionService.getFavoriteSessions(userId);
//        return ResponseEntity.status(429).build();
//    }

    @PatchMapping("/{sessionId}/title")
    @Operation(summary = "Rename a chat session")
    public ResponseEntity<ChatSessionDTO> renameSession(
            @RequestHeader("X-API-Key") String apiKey,
            @PathVariable UUID sessionId,
            @RequestParam String title) {
        ChatSessionDTO chatSessionDTO = chatSessionService.renameSession(sessionId, title);
        return new ResponseEntity<>(chatSessionDTO, HttpStatus.CREATED);
    }


    @PatchMapping("/{sessionId}/favorite")
    @Operation(summary = "Toggle favorite status of a session")
    public ResponseEntity<ChatSessionDTO> toggleFavorite(
            @RequestHeader("X-API-Key") String apiKey,
            @PathVariable UUID sessionId) {
        ChatSessionDTO chatSessionDTO = chatSessionService.toggleFavorite(sessionId);
        return new ResponseEntity<>(chatSessionDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{sessionId}")
    @Operation(summary = "Delete a chat session")
    public ResponseEntity<Void> deleteSession(
            @RequestHeader("X-API-Key") String apiKey,
            @PathVariable UUID sessionId) {
        chatSessionService.deleteSession(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}