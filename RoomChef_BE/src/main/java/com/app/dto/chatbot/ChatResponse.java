package com.app.dto.chatbot;

import java.util.List;
import com.app.dto.fridge.FridgeMatchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String reply;                       // AI 챗봇 친절한 자취 가이드 답변
    private List<FridgeMatchResult> recommendations; // 추천 레시피 (완전/부분 일치)
    private List<String> substituteTips;        // 추천 대체 식재료
    private List<String> additionalShoppingList; // 부족해서 사야 하는 장보기 목록
}
