package com.app.service.evaluation;

import java.util.List;
import com.app.dto.evaluation.RecipeEvaluations;

public interface EvaluationService {
    int submitEvaluation(Long userId, Long recipeId, Double rating, String feedbackNote);
    RecipeEvaluations getMyEvaluation(Long userId, Long recipeId);
    List<RecipeEvaluations> getRecipeEvaluations(Long recipeId);
    int recalculateAllRecipeWeights();
}
