package com.example.chat.service;

import com.example.chat.exception.UnsupportedFileTypeException;
import com.example.chat.exception.VectorStoreException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.chat.constant.Constants.SUPPORTED_EXTENSIONS;
import static com.example.chat.constant.Constants.SUPPORTED_TEXT_TYPES;

@Service
public class FileProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessingService.class);

    private final VectorStore vectorStore;

    public FileProcessingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void processTextFile(MultipartFile file) throws IOException {
        String content = extractContent(file);
        processTextContent(content);
    }

    public void processTextContent(String content) {
        logger.info("inside processTextContent to process file in batch");
        List<Document> documents = List.of(new Document(content));
        try {
            vectorStore.add(documents);
            logger.info("documents saved successfully in vector store");
        }catch (Exception ex){
            logger.error("error occurred while save document to vector store");
            throw new VectorStoreException(ex.getMessage());
        }

    }

    public String extractContent(MultipartFile file) throws IOException {
        logger.info("inside extractContent to process file");
        String filename = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        String contentType = file.getContentType();

        logger.info("Validate file type");
        if (!isSupportedFileType(filename, contentType)) {
            logger.error("Unsupported file type: {}", filename);
            throw new UnsupportedFileTypeException("Unsupported file type: " + filename);
        }

        if (filename.endsWith(".pdf")) {
            return extractTextFromPdf(file.getInputStream());
        } else if (filename.endsWith(".doc") || filename.endsWith(".docx")) {
            return extractTextFromWord(file.getInputStream(), filename);
        } else if (SUPPORTED_TEXT_TYPES.contains(contentType) ||
                filename.endsWith(".txt") || filename.endsWith(".md")) {
            logger.info("extract text from text or md");
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } else {
            logger.error("Unsupported file type: {}", filename);
            throw new UnsupportedFileTypeException("Unsupported file type: " + filename);
        }
    }

    private String extractTextFromPdf(InputStream inputStream) throws IOException {
        logger.info("extract text from Pdf");
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractTextFromWord(InputStream inputStream, String filename) throws IOException {
        logger.info("extract text from doc(s)");
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {
            return doc.getParagraphs().stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n"));
        }
    }

    public boolean isSupportedFileType(String filename, String contentType) {
        return SUPPORTED_EXTENSIONS.stream().anyMatch(filename::endsWith) ||
                SUPPORTED_TEXT_TYPES.contains(contentType);
    }
}