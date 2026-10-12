package com.app.dto.recipe;

import lombok.Data;

@Data
public class RecipeIngredients {
    private Long ingredientId;
    private Long recipeId;
    private String ingredientName;
    private String amount;          // ex: 1/2캔, 1스푼
    private String unit;            // ex: 캔, 스푼, 개
    private String isEssential;     // Y/N (필수 재료 여부)
    private String isBasicSauce;    // Y/N (자취 기본 양념 여부)
}
