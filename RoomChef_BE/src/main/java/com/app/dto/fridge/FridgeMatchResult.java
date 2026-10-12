package com.app.dto.fridge;

import java.util.List;
import com.app.dto.recipe.Recipes;
import lombok.Data;

@Data
public class FridgeMatchResult {
    private Recipes recipe;
    private int totalEssentialIngredients; // 전체 필수 재료 수
    private int matchedIngredientsCount;   // 냉장고에 보유한 재료 수
    private int missingIngredientsCount;   // 부족한 재료 수
    private double matchPercentage;         // 일치율 (%)
    private boolean isFullyMatch;           // 완전 가능 여부 (완전 일치)
    private List<String> matchedIngredients; // 보유 중인 재료명 목록
    private List<String> missingIngredients; // 추가로 사야 하는 재료명 목록
    private List<String> substituteTips;     // 대체 가능한 재료 안내
}
