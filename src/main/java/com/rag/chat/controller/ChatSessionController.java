package com.rag.chat.controller;


import com.rag.chat.dto.ChatSessionDTO;
import com.rag.chat.dto.CreateSessionRequest;
import com.rag.chat.service.ChatSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stringtemplate.v4.ST;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Chat Sessions")
public class ChatSessionController {

    private static final Logger logger = LoggerFactory.getLogger(ChatSessionController.class);

    private final ChatSessionService chatSessionService;

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


    @GetMapping
    @Operation(summary = "Get all sessions for a user")
    public ResponseEntity<List<ChatSessionDTO>> getSessions(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestParam String userId) {
        List<ChatSessionDTO> chatSessionDTOS=chatSessionService.getSessionsByUserId(userId);
        return new ResponseEntity<>(chatSessionDTOS, HttpStatus.OK);
    }

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
    public ResponseEntity<String> deleteSession(
            @RequestHeader("X-API-Key") String apiKey,
            @PathVariable UUID sessionId) {
        chatSessionService.deleteSession(sessionId);
        return new ResponseEntity<String>("Session deleted successfully with ID: "+sessionId,HttpStatus.OK);
    }

//    @GetMapping
//    @Operation(summary = "Get all sessions")
//    public ResponseEntity<List<ChatSession>> getAllSessions(
//            @RequestHeader("X-API-Key") String apiKey,@RequestParam String userId) {
//        List<ChatSession> chatSessions =chatSessionService.getALlSessions();
//        ChatSessionDTO chatSessionDTO=new ChatSessionDTO();
////        chatSessions.forEach(x->
////                chatSessionDTO.setMessageDTOList(x.get)
////        );
//
//        return new ResponseEntity<>(chatSessions, HttpStatus.OK);
//    }
}