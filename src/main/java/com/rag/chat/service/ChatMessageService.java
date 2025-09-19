package com.rag.chat.service;

import com.rag.chat.constant.SenderType;
import com.rag.chat.dto.AddMessageRequest;
import com.rag.chat.dto.ChatMessageDTO;
import com.rag.chat.model.ChatMessage;
import com.rag.chat.model.ChatSession;
import com.rag.chat.repository.ChatMessageRepository;
import com.rag.chat.repository.ChatSessionRepository;
import com.rag.chat.util.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ModelMapper mapper;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatSessionRepository chatSessionRepository;
    private final RagService ragService;

    public ChatMessageService(ModelMapper mapper, ChatMessageRepository chatMessageRepository, ChatSessionRepository chatSessionRepository, RagService ragService) {
        this.mapper = mapper;
        this.chatMessageRepository = chatMessageRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.ragService = ragService;
    }

    @Transactional
    public ChatMessageDTO addMessage(AddMessageRequest request) {
        logger.info("Attempting to add message for session with session ID : {}",request.getSessionId());

        ChatSession session = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> ExceptionUtils.sessionNotFound(request.getSessionId()));
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

            String content="sample resp for testing";;
//            String content=ragService.generateResponse(message.getContent());
            ChatMessage chatMessageResp=new ChatMessage();
            chatMessageResp.setSession(session);
            chatMessageResp.setSender(SenderType.ASSISTANT.name());
            chatMessageResp.setContent(content);
            chatMessageResp.setContext(message.getContext());

            logger.info("Attempting to save open API response ");

            chatMessageResp=chatMessageRepository.save(chatMessageResp);

            logger.info("ASSISTANT message added successfully");

            return mapper.map(chatMessageResp,ChatMessageDTO.class);

        }catch (Exception ex){
            logger.error("An db error occurred {}",ex.getMessage());
            throw ExceptionUtils.dataBaseException();
        }
    }

    public Page<ChatMessageDTO> getMessageHistory(UUID sessionId, Pageable pageable) {
        return chatMessageRepository.findBySessionId(sessionId, pageable)
                .map(x->mapper.map(x,ChatMessageDTO.class));
    }
}