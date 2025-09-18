package com.example.chat.service;

import com.example.chat.constant.SenderType;
import com.example.chat.dto.AddMessageRequest;
import com.example.chat.dto.ChatMessageDTO;
import com.example.chat.exception.RagChatDBException;
import com.example.chat.exception.SessionNotFoundException;
import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatSession;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatSessionRepository chatSessionRepository;
    private final RagService ragService;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatSessionRepository chatSessionRepository, RagService ragService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.ragService = ragService;
    }

    @Transactional
    public ChatMessageDTO addMessage(AddMessageRequest request) {
        logger.info("Attempting to add message for session with session ID : {}",request.getSessionId());

        ChatSession session = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> {
                    logger.error("Session not found with ID: {}", request.getSessionId());
                    return new SessionNotFoundException(request.getSessionId());
                });

        try {
            logger.info("Attempting to create user message");
            ChatMessage message = new ChatMessage();
            message.setSession(session);
            message.setSender(request.getSender());
            message.setContent(request.getContent());
            message.setContext(request.getContext());

            message=chatMessageRepository.save(message);

            logger.info("user message added successfully");

            // get resp from open AI
            logger.info("calling to open AI to get response for user message ");
            String content=ragService.generateResponse(message.getContent());
            logger.info("open AI response for user message {} ",content);
            ChatMessage chatMessageResp=new ChatMessage();
            chatMessageResp.setSession(session);
            chatMessageResp.setSender("ASSISTANT");
            chatMessageResp.setContent(content);
            chatMessageResp.setContext(message.getContext());

            logger.info("Attempting to save open API response ");

            chatMessageResp=chatMessageRepository.save(chatMessageResp);

            logger.info("ASSISTANT message added successfully");
            return convertToDTO(chatMessageResp);
        }catch (Exception ex){
            logger.error("An db error occurred {}",ex.getMessage());
            throw new RagChatDBException(ex.getMessage());
        }

    }

    public Page<ChatMessageDTO> getMessageHistory(UUID sessionId, Pageable pageable) {
        return chatMessageRepository.findBySessionId(sessionId, pageable)
                .map(this::convertToDTO);
    }
    
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setSessionId(message.getSession().getId());
        dto.setSender(message.getSender());
        dto.setContent(message.getContent());
        dto.setContext(message.getContext());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}