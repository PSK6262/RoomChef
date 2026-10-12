package com.app.controller.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.ResultCode;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.dto.recipe.Recipes;
import com.app.service.recipe.RecipeService;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Recipes>>> getRecipeList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sourceType,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String onePanYn) {

        Map<String, Object> params = new HashMap<>();
        if (keyword != null && !keyword.isBlank()) params.put("keyword", keyword.trim());
        if (sourceType != null && !sourceType.isBlank()) params.put("sourceType", sourceType.trim());
        if (difficulty != null && !difficulty.isBlank()) params.put("difficulty", difficulty.trim());
        if (onePanYn != null && !onePanYn.isBlank()) params.put("onePanYn", onePanYn.trim());

        List<Recipes> list = recipeService.getRecipeList(params);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<Recipes>> getRecipeDetail(@PathVariable Long recipeId) {
        Recipes recipe = recipeService.getRecipeDetail(recipeId);
        if (recipe == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ResultCode.RECIPE_NOT_FOUND));
        }
        return ResponseEntity.ok(ApiResponse.success(recipe));
    }

    @PostMapping("/match")
    public ResponseEntity<ApiResponse<List<FridgeMatchResult>>> matchRecipes(
            @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> ingredients = (List<String>) body.get("ingredients");
        Boolean allowPartial = (Boolean) body.getOrDefault("allowPartial", true);

        List<FridgeMatchResult> results = recipeService.matchRecipesByIngredients(ingredients, allowPartial != null && allowPartial);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
