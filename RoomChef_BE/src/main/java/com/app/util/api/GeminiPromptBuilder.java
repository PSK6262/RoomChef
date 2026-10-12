package com.app.util.api;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * [Gemini 프롬프트 템플릿 빌더 - GeminiPromptBuilder]
 * 
 * - 자취요리 전문 AI 챗봇 질문 프롬프트 조립
 * - 유튜브 자막/설명란에서 정형화된 1인분 레시피 JSON 추출 프롬프트 조립
 */
@Component
public class GeminiPromptBuilder {

    /**
     * 자취생 냉장고 파먹기 및 요리 추천 챗봇 프롬프트 생성
     */
    public String buildRecipeChatPrompt(String userMessage, List<String> currentIngredients, String situation) {
        String ingredientsStr = (currentIngredients != null && !currentIngredients.isEmpty()) 
                ? String.join(", ", currentIngredients) 
                : "냉장고 등록 재료 없음";

        String situationStr = (situation != null && !situation.isEmpty()) ? situation : "일반 자취 요리";

        return """
            당신은 1인 가구 자취생을 위한 다정하고 실용적인 요리 전문 AI 비서 '방구석 셰프'입니다.
            자취생의 현실(원팬 조리 선호, 적은 설거지, 1인분 기준, 최소한의 재료, 대체 조미료 안내)을 깊이 이해하고 답변하세요.

            [사용자 상황 정보]
            - 현재 보유 중인 재료: %s
            - 상황/키워드: %s
            - 질문 내용: %s

            [답변 원칙]
            1. '완전 가능 요리'(보유 재료로 바로 만들 수 있는 요리)와 '부분 가능 요리'(1~2개만 더 사면 가능한 요리)를 명확히 구분하여 추천하세요.
            2. 부족한 재료가 있을 경우, 집에 흔히 있는 양념(간장, 설탕, 다시다, 식용유 등)으로 대체할 수 있는 '대체 꿀팁'을 반드시 포함하세요.
            3. 조리 과정은 3~5단계 이내로 매우 쉽고 간결하게 설명하세요.
            4. 말투는 친근하고 실용적인 자취 선배 같은 부드러운 톤앤매너로 작성하세요.
            """.formatted(ingredientsStr, situationStr, userMessage);
    }

    /**
     * 유튜브 영상 설명 및 자막에서 레시피 데이터 정형화(JSON) 추출 프롬프트
     */
    public String buildYoutubeRecipeExtractionPrompt(String videoTitle, String textContent) {
        return """
            당신은 요리 레시피 데이터 엔지니어입니다.
            제공된 유튜브 영상 제목과 본문/자막 텍스트에서 1인 가구 자취생 기준의 요리 레시피를 정형화된 JSON으로 추출하세요.

            [영상 정보]
            - 제목: %s
            - 텍스트 내용: %s

            [반드시 지켜야 할 JSON 응답 포맷 (순수 JSON만 출력)]
            {
              "title": "요리명",
              "cookTimeMin": 15,
              "difficulty": "EASY",
              "servings": 1,
              "onePanYn": "Y 또는 N",
              "microwaveYn": "Y 또는 N",
              "ingredients": [
                {
                  "name": "재료명",
                  "amount": "1/2캔, 1개 등",
                  "unit": "스푼, 개 등",
                  "isEssential": "Y 또는 N",
                  "isBasicSauce": "Y 또는 N"
                }
              ],
              "steps": [
                {
                  "stepOrder": 1,
                  "instruction": "조리 설명",
                  "timerSeconds": 180,
                  "tip": "팁 내용"
                }
              ]
            }
            """.formatted(videoTitle, textContent);
    }
}
