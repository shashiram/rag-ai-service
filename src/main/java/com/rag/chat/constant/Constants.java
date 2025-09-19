package com.rag.chat.constant;

import java.util.Set;

public class Constants {
    public static final Set<String> SUPPORTED_TEXT_TYPES = Set.of(
            "text/plain", "text/markdown", "text/html", "application/json", "application/xml"
    );

    public static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            ".txt", ".md", ".html", ".htm", ".json", ".xml", ".pdf", ".doc", ".docx"
    );
}
