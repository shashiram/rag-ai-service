package com.rag.chat.service;

import com.rag.chat.dto.ChatSessionDTO;
import com.rag.chat.dto.CreateSessionRequest;
import com.rag.chat.model.ChatMessage;
import com.rag.chat.model.ChatSession;
import com.rag.chat.repository.ChatMessageRepository;
import com.rag.chat.repository.ChatSessionRepository;
import com.rag.chat.util.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatSessionService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatSessionRepository chatSessionRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final ModelMapper mapper;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository, ModelMapper mapper) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.mapper = mapper;
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
            return mapper.map(session,ChatSessionDTO.class);
        } catch (Exception ex) {
            logger.error("An db error occurred {}", ex.getMessage());
            throw ExceptionUtils.dataBaseException();
        }
    }

    @Transactional(readOnly = true)
    public List<ChatSessionDTO> getSessionsByUserId(String userId) {
        logger.info("Attempting to get getSessionsByUserId to  {}",userId);
        List<ChatSession> sessions =chatSessionRepository.findByUserId(userId);
        if(sessions.isEmpty()){
            throw ExceptionUtils.userSessionNotFoundException();
        }
        return mapper.map(sessions,new TypeToken<List<ChatSessionDTO>>() {}.getType());
    }


    @Transactional
    public ChatSessionDTO renameSession(UUID sessionId, String newTitle) {
        logger.info("Attempting to rename session to  {}", newTitle);
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> ExceptionUtils.sessionNotFound(sessionId));

        try {
            session.setTitle(newTitle);
            chatSessionRepository.save(session);
            logger.info("Session with ID: {} renamed successfully", sessionId);
            return mapper.map(session,ChatSessionDTO.class);
        } catch (Exception ex) {
            logger.error("session rename error {}", ex.getMessage());
            throw ExceptionUtils.dataBaseException();
        }

    }

    @Transactional
    public ChatSessionDTO toggleFavorite(UUID sessionId) {

        logger.info("Setting to Favorite session to  {}", sessionId);
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> ExceptionUtils.sessionNotFound(sessionId));
        try {
            chatSessionRepository.updateFavoriteStatus(sessionId, !session.isFavorite());
            session.setFavorite(!session.isFavorite());

            logger.info("Session with ID: {} set to favorite successfully", sessionId);
            return mapper.map(session,ChatSessionDTO.class);
        }catch (Exception ex){
            logger.error("session toggle Favorite error {}", ex.getMessage());
            throw ExceptionUtils.dataBaseException();
        }

    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        logger.info("inside to deleteSession with ID  {}", sessionId);
        // batch delete of chat messages for better performance
        if (!chatSessionRepository.existsById(sessionId)) {
            logger.error("Session not found with ID: {}", sessionId);
           // throw new SessionNotFoundException(sessionId);
           throw  ExceptionUtils.sessionNotFound(sessionId);
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
            throw ExceptionUtils.sessionNotFound(sessionId);
        }
    }
}