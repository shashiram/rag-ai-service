package com.rag.chat.controller;


import com.rag.chat.dto.AddMessageRequest;
import com.rag.chat.dto.ChatMessageDTO;
import com.rag.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Chat Messages")
public class ChatMessageController {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    @Operation(summary = "Add a message to a chat session")
    public ResponseEntity<ChatMessageDTO> addMessage(
            @RequestHeader("X-API-Key") String apiKey,
            @Valid @RequestBody AddMessageRequest messageRequest) {
        logger.info("Attempting Add a message to a chat session");
        ChatMessageDTO chatMessageDTO= chatMessageService.addMessage(messageRequest);
        return new ResponseEntity<>(chatMessageDTO, HttpStatus.CREATED);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated message history for a session")
    public ResponseEntity<Page<ChatMessageDTO>> getMessageHistoryPaginated(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestParam UUID sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        logger.info("Attempting Get paginated message history for a session {}",sessionId);

        Pageable pageable=PageRequest.of(page,size, Sort.by("createdAt").descending());

        Page<ChatMessageDTO> chatMessageDTOPage=chatMessageService.getMessageHistory(sessionId,pageable);
        return new ResponseEntity<>(chatMessageDTOPage, HttpStatus.OK);
    }
}