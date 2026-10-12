package com.app.service.chatbot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.chatbot.ChatRequest;
import com.app.dto.chatbot.ChatResponse;
import com.app.dto.fridge.FridgeIngredients;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.service.chatbot.ChatbotService;
import com.app.service.fridge.FridgeService;
import com.app.service.recipe.RecipeService;
import com.app.util.api.GeminiApiClient;
import com.app.util.api.GeminiApiClient.GeminiCallResult;
import com.app.util.api.GeminiPromptBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatbotServiceImpl implements ChatbotService {

    @Autowired(required = false)
    private GeminiApiClient geminiApiClient;

    @Autowired
    private GeminiPromptBuilder geminiPromptBuilder;

    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private RecipeService recipeService;

    @Override
    public ChatResponse processUserChat(Long userId, ChatRequest request) {
        List<String> targetIngredients = new ArrayList<>();

        // 1. 요청에 직접 전달된 재료가 있으면 우선 사용, 없으면 로그인 유저의 냉장고 재료 조회
        if (request.getCurrentIngredients() != null && !request.getCurrentIngredients().isEmpty()) {
            targetIngredients.addAll(request.getCurrentIngredients());
        } else if (userId != null) {
            List<FridgeIngredients> fridgeList = fridgeService.getUserIngredients(userId);
            if (fridgeList != null) {
                targetIngredients.addAll(fridgeList.stream()
                        .map(FridgeIngredients::getIngredientName)
                        .collect(Collectors.toList()));
            }
        }

        // 2. DB 기반 레시피 매칭 수행 (완전 일치 + 2개 이하 부족한 부분 일치)
        List<FridgeMatchResult> matchResults = recipeService.matchRecipesByIngredients(targetIngredients, true);

        // 3. Gemini API 프롬프트 조립 및 호출
        String replyText = "";
        try {
            if (geminiApiClient != null) {
                String prompt = geminiPromptBuilder.buildRecipeChatPrompt(
                        request.getMessage(), targetIngredients, request.getMoodOrSituation());
                String text = geminiApiClient.callGemini(prompt);
                if (text != null) {
                    replyText = text;
                }
            }
        } catch (Exception e) {
            log.warn("[ChatbotService] Gemini API 호출 실패(폴백 모드 동작): {}", e.getMessage());
        }

        // 4. Gemini 미설정 또는 호출 실패 시 친절한 룰 기반 기본 응답 생성
        if (replyText == null || replyText.trim().isEmpty()) {
            if (matchResults.isEmpty()) {
                replyText = "보유하신 재료로 바로 만들 수 있는 요리를 찾지 못했어요. 양파나 계란, 간장 같은 기본 재료를 추가해보시는 건 어떨까요?";
            } else {
                FridgeMatchResult topMatch = matchResults.get(0);
                if (topMatch.isFullyMatch()) {
                    replyText = "현재 냉장고 재료로 바로 만들 수 있는 요리로 '" + topMatch.getRecipe().getTitle() + "'을(를) 추천드려요! 원팬으로 빠르게 뚝딱 만드실 수 있습니다.";
                } else {
                    replyText = "'" + topMatch.getRecipe().getTitle() + "' 요리를 추천드려요! " +
                            String.join(", ", topMatch.getMissingIngredients()) + "만 추가로 준비하시면 완벽합니다.";
                }
            }
        }

        // 5. 부족한 재료 장보기 목록 및 대체 팁 수집
        List<String> shoppingList = new ArrayList<>();
        List<String> substituteTips = new ArrayList<>();
        for (FridgeMatchResult r : matchResults) {
            if (r.getMissingIngredients() != null) {
                for (String m : r.getMissingIngredients()) {
                    if (!shoppingList.contains(m)) {
                        shoppingList.add(m);
                    }
                }
            }
            if (r.getSubstituteTips() != null) {
                for (String tip : r.getSubstituteTips()) {
                    if (!substituteTips.contains(tip)) {
                        substituteTips.add(tip);
                    }
                }
            }
        }

        return ChatResponse.builder()
                .reply(replyText)
                .recommendations(matchResults)
                .substituteTips(substituteTips)
                .additionalShoppingList(shoppingList)
                .build();
    }
}
