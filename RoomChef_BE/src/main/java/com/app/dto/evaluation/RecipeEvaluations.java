package com.app.dto.evaluation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class RecipeEvaluations {
    private Long evalId;
    private Long userId;
    private Long recipeId;
    private Double rating;        // 1.0 ~ 5.0 점
    private String feedbackNote;
    private LocalDateTime evaluatedAt;

    // 조인용 필드
    private String recipeTitle;
    private String userNickname;

    public String getEvaluatedAt() {
        if (this.evaluatedAt == null) return null;
        return this.evaluatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
