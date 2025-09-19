package com.rag.chat.repository;


import com.rag.chat.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySessionId(UUID sessionId);
    Page<ChatMessage> findBySessionId(UUID sessionId, Pageable pageable);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.session.id = :sessionId ORDER BY cm.createdAt ASC")
    List<ChatMessage> findMessageHistory(@Param("sessionId") UUID sessionId);
    
    void deleteBySessionId(UUID sessionId);
}