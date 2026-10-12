package com.app.service.recipe;

import java.util.List;
import java.util.Map;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.dto.recipe.Recipes;

public interface RecipeService {
    List<Recipes> getRecipeList(Map<String, Object> filterParams);
    Recipes getRecipeDetail(Long recipeId);
    List<FridgeMatchResult> matchRecipesByIngredients(List<String> userIngredients, boolean allowPartial);
}
