package com.app.util.api;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * [Gemini 응답 파서 - GeminiResponseParser]
 */
@Slf4j
@Component
public class GeminiResponseParser {

    private final ObjectMapper objectMapper;

    public GeminiResponseParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 마크다운 백틱(```json ... ```) 제거 후 순수 텍스트/JSON 문자열 추출
     */
    public String cleanMarkdownJson(String rawText) {
        if (rawText == null) return "{}";
        String cleaned = rawText.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    /**
     * JSON 파싱 시도
     */
    public JsonNode parseToJsonNode(String jsonString) {
        try {
            return objectMapper.readTree(cleanMarkdownJson(jsonString));
        } catch (Exception e) {
            log.error("[GeminiResponseParser] JSON 파싱 실패: {}", e.getMessage());
            return null;
        }
    }
}
