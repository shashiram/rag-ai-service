package com.rag.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RagChatServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagChatServiceApplication.class, args);
    }
}
