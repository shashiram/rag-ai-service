package com.rag.chat.service;

import com.rag.chat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {
    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public String generateResponse(String query) {

        logger.info("call similaritySearch for query {}",query);
        List<Document> documents = this.vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(5).build());
        String context = buildContext(documents);
        Message similarDocsMessage = new SystemPromptTemplate("Based on the following: {documents}")
                .createMessage(Map.of("documents", context));

        logger.info("prompt generation ");
        Prompt prompt = new Prompt(List.of(similarDocsMessage, new UserMessage(query)));

        try {
            logger.info("calling open api ");
            String content=this.chatClient.prompt(prompt).call().content();
            logger.info("response from chat client call : {}",content);
            return content;
        }catch (Exception ex){
            logger.error("error from chat client: {}",ex.getMessage());
            throw ExceptionUtils.openAIClientApiCallError();
        }
    }

    private String buildContext(List<Document> documents) {
        logger.info("attempting to build context");
        return documents.stream().map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}