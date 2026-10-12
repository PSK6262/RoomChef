package com.app.dao.recipe;

import java.util.List;
import java.util.Map;
import com.app.dto.recipe.RecipeIngredients;
import com.app.dto.recipe.RecipeSteps;
import com.app.dto.recipe.Recipes;

public interface RecipeDAO {
    List<Recipes> selectRecipeList(Map<String, Object> params);
    Recipes selectRecipeById(Long recipeId);
    List<RecipeIngredients> selectRecipeIngredients(Long recipeId);
    List<RecipeSteps> selectRecipeSteps(Long recipeId);
    int insertRecipe(Recipes recipe);
    int insertRecipeIngredient(RecipeIngredients ingredient);
    int insertRecipeStep(RecipeSteps step);
    int incrementViewCount(Long recipeId);
    List<Recipes> selectAllRecipesWithIngredients();
}
