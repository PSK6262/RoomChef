package com.app.dto.chatbot;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message;
    private List<String> currentIngredients; // 보유 재료 목록(선택사항)
    private String moodOrSituation;        // 상황(예: 해장, 야식, 월급전날, 원팬)
}
