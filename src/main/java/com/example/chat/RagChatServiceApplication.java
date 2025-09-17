package com.example.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RagChatServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagChatServiceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initDatabase(DocumentChunkRepository repository) {
//        return args -> {
//            // Optional: Add some initial data or perform setup
//        };
//    }
}
