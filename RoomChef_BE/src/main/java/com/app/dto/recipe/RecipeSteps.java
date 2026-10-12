package com.app.dto.recipe;

import lombok.Data;

@Data
public class RecipeSteps {
    private Long stepId;
    private Long recipeId;
    private Integer stepOrder;
    private String instruction;
    private Integer timerSeconds;   // 조리 타이머 (초 단위)
    private String tip;
}
