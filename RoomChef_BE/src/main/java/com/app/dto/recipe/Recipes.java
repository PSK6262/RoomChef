package com.app.dto.recipe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Data;

@Data
public class Recipes {
    private Long recipeId;
    private String title;
    private String sourceType;     // YOUTUBE, PUBLIC_API, USER_CUSTOM
    private String channelName;    // 자취요리신, 뚝딱이형 등
    private String videoUrl;
    private String thumbnailUrl;
    private Integer cookTimeMin;
    private String difficulty;     // EASY, MEDIUM, HARD
    private Integer servings;      // 인분 (기본 1인분)
    private String onePanYn;       // 원팬 여부 (Y/N)
    private String microwaveYn;    // 전자레인지 여부 (Y/N)
    private Double scoreWeight;    // 추천 노출 가중치 (기본 1.0)
    private Long viewCount;
    private Double avgRating;      // 평균 평점 (1.0 ~ 5.0)
    private Integer evalCount;     // 평가 참여 인원

    private LocalDateTime createdAt;

    // 상세 조회용 하위 데이터
    private List<RecipeIngredients> ingredients;
    private List<RecipeSteps> steps;

    public String getCreatedAt() {
        if (this.createdAt == null) return null;
        return this.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
