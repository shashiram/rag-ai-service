package com.example.chat.repository;

import com.example.chat.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<ChatSession> findByUserId(String userId, Pageable pageable);
    List<ChatSession> findByUserIdAndIsFavorite(String userId, boolean isFavorite);
    
    @Modifying
    @Query("UPDATE ChatSession cs SET cs.title = :title WHERE cs.id = :id")
    void updateTitle(@Param("id") UUID id, @Param("title") String title);
    
    @Modifying
    @Query("UPDATE ChatSession cs SET cs.isFavorite = :isFavorite WHERE cs.id = :id")
    void updateFavoriteStatus(@Param("id") UUID id, @Param("isFavorite") boolean isFavorite);
}