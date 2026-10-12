package com.app.controller.chatbot;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.CommonCode;
import com.app.common.ResultCode;
import com.app.dto.chatbot.ChatRequest;
import com.app.dto.chatbot.ChatResponse;
import com.app.dto.user.Users;
import com.app.service.chatbot.ChatbotService;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<ChatResponse>> askChatbot(
            @RequestBody ChatRequest req, HttpServletRequest request) {

        if (req == null || req.getMessage() == null || req.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ResultCode.INVALID_INPUT));
        }

        Users user = (Users) request.getAttribute(CommonCode.SESSION_LOGIN_USER);
        Long userId = (user != null) ? user.getUserId() : null;

        ChatResponse response = chatbotService.processUserChat(userId, req);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
