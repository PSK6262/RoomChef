package com.app.service.chatbot;

import com.app.dto.chatbot.ChatRequest;
import com.app.dto.chatbot.ChatResponse;

public interface ChatbotService {
    ChatResponse processUserChat(Long userId, ChatRequest request);
}
