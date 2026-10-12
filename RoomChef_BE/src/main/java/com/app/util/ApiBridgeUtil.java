package com.app.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * [RoomChef 요리 데이터 연결 및 변환 유틸리티 - ApiBridgeUtil]
 * 
 * 1. 자취 필수 기본 양념 판별 (집에 상시 구비된 조미료 자동 분류)
 * 2. 식재료 대체재(Substitute) 매핑 딕셔너리
 * 3. 1인분 계량 스케일링 & 밥숟가락/종이컵 단위 환산
 */
public class ApiBridgeUtil {

    // 1. 자취방 상시 기본 양념 목록 (냉장고 등록 여부와 무관하게 기본 보유로 분류)
    private static final Set<String> BASIC_SAUCE_SET = new HashSet<>(Arrays.asList(
        "진간장", "양조간장", "간장", "국간장", "식용유", "카놀라유", "설탕", "소금",
        "고춧가루", "후추", "참기름", "다진마늘", "마늘", "라면스프", "물"
    ));

    // 2. 대체 식재료 매핑 딕셔너리 (없는 재료 -> 대체 가능 조합)
    private static final Map<String, String> SUBSTITUTE_MAP = new HashMap<>();

    static {
        SUBSTITUTE_MAP.put("굴소스", "진간장 1스푼 + 설탕 0.5스푼 + 다시다 약간");
        SUBSTITUTE_MAP.put("맛술", "소주 1스푼 + 설탕 1/3스푼");
        SUBSTITUTE_MAP.put("미림", "소주 1스푼 + 설탕 1/3스푼");
        SUBSTITUTE_MAP.put("치킨스톡", "다시다 0.5스푼 또는 연두 0.5스푼");
        SUBSTITUTE_MAP.put("버터", "마가린 또는 식용유");
        SUBSTITUTE_MAP.put("올리브유", "일반 식용유 또는 카놀라유");
        SUBSTITUTE_MAP.put("생강", "다진마늘로 대체하거나 생략 가능");
        SUBSTITUTE_MAP.put("파슬리", "생략 가능 (단순 고명용)");
        SUBSTITUTE_MAP.put("청주", "소주 또는 물로 대체 가능");
        SUBSTITUTE_MAP.put("물엿", "올리고당 또는 설탕 0.7스푼");
        SUBSTITUTE_MAP.put("올리고당", "물엿 또는 설탕 0.7스푼");
        SUBSTITUTE_MAP.put("참치액", "액젓 0.5스푼 또는 진간장");
    }

    /**
     * 자취 기본 양념인지 여부 확인
     */
    public static boolean isBasicSauce(String ingredientName) {
        if (ingredientName == null) return false;
        String normalized = ingredientName.trim().replaceAll("\\s+", "");
        for (String sauce : BASIC_SAUCE_SET) {
            if (normalized.contains(sauce)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 부족한 식재료에 대한 대체재 팁 반환
     */
    public static String getSubstituteTip(String ingredientName) {
        if (ingredientName == null) return null;
        String normalized = ingredientName.trim();
        for (Map.Entry<String, String> entry : SUBSTITUTE_MAP.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return entry.getKey() + "이(가) 없으면: " + entry.getValue();
            }
        }
        return null;
    }

    /**
     * N인분 레시피 재료 양을 1인분 기준으로 자동 스케일링
     */
    public static String scaleToSinglePortion(String amount, int originalServings) {
        if (amount == null || originalServings <= 1) return amount;
        try {
            // 숫자가 포함된 경우 단순 비율 계산
            String numericPart = amount.replaceAll("[^0-9.]", "");
            if (!numericPart.isEmpty()) {
                double val = Double.parseDouble(numericPart);
                double scaled = Math.round((val / originalServings) * 10.0) / 10.0;
                String unitPart = amount.replaceAll("[0-9.]", "").trim();
                return scaled + unitPart;
            }
        } catch (Exception ignored) {
        }
        return amount + " (" + originalServings + "인분 기준)";
    }

    public static Map<String, String> getAllSubstitutes() {
        return Collections.unmodifiableMap(SUBSTITUTE_MAP);
    }
}
