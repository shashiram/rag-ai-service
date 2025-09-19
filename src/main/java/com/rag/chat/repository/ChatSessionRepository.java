package com.rag.chat.repository;

import com.rag.chat.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    List<ChatSession> findByUserId(String userId);

    @Modifying
    @Query("UPDATE ChatSession cs SET cs.title = :title WHERE cs.id = :id")
    void updateTitle(@Param("id") UUID id, @Param("title") String title);
    
    @Modifying
    @Query("UPDATE ChatSession cs SET cs.isFavorite = :isFavorite WHERE cs.id = :id")
    void updateFavoriteStatus(@Param("id") UUID id, @Param("isFavorite") boolean isFavorite);

    //    @Query("SELECT cs from ChatSession cs JOIN FETCH cs.chatMessages")
//    List<ChatSession> finALlSessions();
}