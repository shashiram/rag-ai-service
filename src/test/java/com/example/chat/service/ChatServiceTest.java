package com.example.chat.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChatServiceTest {

//    @Mock
//    private ChatSessionRepository sessionRepo;
//
//    @Mock
//    private MessageRepository messageRepo;
//
//    @InjectMocks
//    private ChatService chatService;
//
//    public ChatServiceTest() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateSession() {
//        ChatSession s = new ChatSession();
//       // s.setName("Hello");
//
//        when(sessionRepo.save(any(ChatSession.class))).thenReturn(s);
//
//        ChatSession created = chatService.createSession("Hello");
//        assertNotNull(created);
//       // assertEquals("Hello", created.getName());
//        verify(sessionRepo, times(1)).save(any(ChatSession.class));
//    }
//
//    @Test
//    public void testAddMessage() {
//        UUID sid = UUID.randomUUID();
//        Message m = new Message();
//        m.setContent("Hi");
//        when(messageRepo.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));
//
//        Message out = chatService.addMessage(sid, m);
//        //assertEquals(sid, out.getSessionId());
//        assertEquals("Hi", out.getContent());
//        verify(messageRepo, times(1)).save(any(Message.class));
//    }
}
