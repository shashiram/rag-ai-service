package com.example.chat.service;

import com.example.chat.dto.ChatSessionDTO;
import com.example.chat.dto.CreateSessionRequest;
import com.example.chat.exception.RagChatDBException;
import com.example.chat.exception.SessionNotFoundException;
import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatSession;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatSessionService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatSessionRepository chatSessionRepository;

    private final ChatMessageRepository chatMessageRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public ChatSessionDTO createSession(CreateSessionRequest sessionRequest) {

        try {
            logger.info("Attempting to create session");
            ChatSession session = new ChatSession();
            session.setUserId(sessionRequest.getUserId());
            session.setTitle(sessionRequest.getTitle());
            session = chatSessionRepository.save(session);

            logger.info("Session created successfully with ID: {}", session.getId());
            return convertToDTO(session);
        } catch (Exception ex) {
            logger.error("An db error occurred {}", ex.getMessage());
            throw new RagChatDBException(ex.getMessage());
        }
    }


    @Transactional
    public ChatSessionDTO renameSession(UUID sessionId, String newTitle) {
        logger.info("Attempting to rename session to  {}", newTitle);
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    logger.error("Session not found with ID: {}", sessionId);
                    return new SessionNotFoundException(sessionId);
                });

        try {
            session.setTitle(newTitle);
            chatSessionRepository.save(session);
            logger.info("Session with ID: {} renamed successfully", sessionId);
            return convertToDTO(session);
        } catch (Exception ex) {
            logger.error("session rename error {}", ex.getMessage());
            throw new RagChatDBException(ex.getMessage());
        }

    }

    @Transactional
    public ChatSessionDTO toggleFavorite(UUID sessionId) {

        logger.info("Setting to Favorite session to  {}", sessionId);
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    logger.error("Session not found with ID: {}", sessionId);
                    return new SessionNotFoundException(sessionId);
                });
        try {
            chatSessionRepository.updateFavoriteStatus(sessionId, !session.isFavorite());
            session.setFavorite(!session.isFavorite());

            logger.info("Session with ID: {} set to favorite successfully", sessionId);
            return convertToDTO(session);
        }catch (Exception ex){
            logger.error("session toggle Favorite error {}", ex.getMessage());
            throw new RagChatDBException(ex.getMessage());
        }

    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        logger.info("inside to deleteSession with ID  {}", sessionId);
        // batch delete of chat messages for better performance
        if (!chatSessionRepository.existsById(sessionId)) {
            logger.error("Session not found with ID: {}", sessionId);
            throw new SessionNotFoundException(sessionId);
        }

        try {
            logger.info("Attempting to delete all chat messages of Session with ID  {}", sessionId);
            // First delete chat messages in batches
            List<UUID> chatMessageIds = chatMessageRepository.findBySessionId(sessionId).stream()
                    .map(ChatMessage::getId)
                    .toList();

            if (!chatMessageIds.isEmpty()) {
                logger.info("Call to delete all chat messages with IDs  {}", chatMessageIds);
                chatMessageRepository.deleteAllByIdInBatch(chatMessageIds);
                logger.info("All chat messages with IDs {} deleted successfully", sessionId);
            }
            logger.info("Attempting to delete Session with ID  {}", sessionId);

            chatSessionRepository.deleteById(sessionId);

            logger.info("Session deleted successfully with ID: {}", sessionId);
        }catch (Exception ex){
            logger.error("Session deletion error {}", ex.getMessage());
            throw new SessionNotFoundException(sessionId);
        }
    }


    private ChatSessionDTO convertToDTO(ChatSession session) {
        ChatSessionDTO dto = new ChatSessionDTO();
        dto.setId(session.getId());
        dto.setUserId(session.getUserId());
        dto.setTitle(session.getTitle());
        dto.setFavorite(session.isFavorite());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());
        return dto;
    }

}