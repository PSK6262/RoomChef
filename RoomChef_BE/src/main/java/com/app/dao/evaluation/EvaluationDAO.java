package com.app.dao.evaluation;

import java.util.List;
import java.util.Map;
import com.app.dto.evaluation.RecipeEvaluations;

public interface EvaluationDAO {
    int insertOrUpdateEvaluation(RecipeEvaluations evaluation);
    RecipeEvaluations selectEvaluationByUserAndRecipe(Long userId, Long recipeId);
    List<RecipeEvaluations> selectEvaluationsByRecipeId(Long recipeId);
    List<Map<String, Object>> selectRecipeAvgRatings();
    int updateRecipeScoreWeight(Long recipeId, Double scoreWeight);
}
