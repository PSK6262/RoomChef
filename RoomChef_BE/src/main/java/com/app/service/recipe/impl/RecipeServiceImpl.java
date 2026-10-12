package com.app.service.recipe.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.recipe.RecipeDAO;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.dto.recipe.RecipeIngredients;
import com.app.dto.recipe.Recipes;
import com.app.service.recipe.RecipeService;
import com.app.util.ApiBridgeUtil;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;

    @Override
    public List<Recipes> getRecipeList(Map<String, Object> filterParams) {
        return recipeDAO.selectRecipeList(filterParams);
    }

    @Override
    public Recipes getRecipeDetail(Long recipeId) {
        recipeDAO.incrementViewCount(recipeId);
        Recipes recipe = recipeDAO.selectRecipeById(recipeId);
        if (recipe != null) {
            recipe.setIngredients(recipeDAO.selectRecipeIngredients(recipeId));
            recipe.setSteps(recipeDAO.selectRecipeSteps(recipeId));
        }
        return recipe;
    }

    @Override
    public List<FridgeMatchResult> matchRecipesByIngredients(List<String> userIngredients, boolean allowPartial) {
        List<Recipes> allRecipes = recipeDAO.selectAllRecipesWithIngredients();
        List<FridgeMatchResult> results = new ArrayList<>();

        if (userIngredients == null) {
            userIngredients = new ArrayList<>();
        }

        // 유저 재료 정규화 (공백 제거)
        List<String> normalizedUserIngredients = userIngredients.stream()
                .map(s -> s.trim().replaceAll("\\s+", ""))
                .toList();

        for (Recipes recipe : allRecipes) {
            List<RecipeIngredients> ingredients = recipe.getIngredients();
            if (ingredients == null || ingredients.isEmpty()) continue;

            List<String> matched = new ArrayList<>();
            List<String> missing = new ArrayList<>();
            List<String> subTips = new ArrayList<>();

            int essentialCount = 0;

            for (RecipeIngredients ing : ingredients) {
                String ingName = ing.getIngredientName();
                boolean isBasic = "Y".equalsIgnoreCase(ing.getIsBasicSauce()) || ApiBridgeUtil.isBasicSauce(ingName);
                boolean isEssential = "Y".equalsIgnoreCase(ing.getIsEssential());

                if (isBasic) {
                    // 기본 양념은 항상 구비된 것으로 간주
                    matched.add(ingName + " (기본양념)");
                    continue;
                }

                if (isEssential) {
                    essentialCount++;
                    boolean hasIngredient = false;
                    for (String userIng : normalizedUserIngredients) {
                        if (ingName.replaceAll("\\s+", "").contains(userIng) || userIng.contains(ingName.replaceAll("\\s+", ""))) {
                            hasIngredient = true;
                            break;
                        }
                    }

                    if (hasIngredient) {
                        matched.add(ingName);
                    } else {
                        missing.add(ingName);
                        String subTip = ApiBridgeUtil.getSubstituteTip(ingName);
                        if (subTip != null) {
                            subTips.add(subTip);
                        }
                    }
                }
            }

            int missingCount = missing.size();
            boolean isFullyMatch = (missingCount == 0);

            // 완전 일치이거나, 부분 일치(2개 이하 부족) 허용일 때만 결과에 추가
            if (isFullyMatch || (allowPartial && missingCount <= 2)) {
                FridgeMatchResult matchResult = new FridgeMatchResult();
                matchResult.setRecipe(recipe);
                matchResult.setTotalEssentialIngredients(essentialCount);
                matchResult.setMatchedIngredientsCount(matched.size());
                matchResult.setMissingIngredientsCount(missingCount);
                matchResult.setFullyMatch(isFullyMatch);
                matchResult.setMatchedIngredients(matched);
                matchResult.setMissingIngredients(missing);
                matchResult.setSubstituteTips(subTips);

                double percentage = (essentialCount > 0)
                        ? Math.round(((double) (essentialCount - missingCount) / essentialCount) * 100.0)
                        : 100.0;
                matchResult.setMatchPercentage(percentage);

                results.add(matchResult);
            }
        }

        // 평점 기반 가중치(scoreWeight DESC) 및 일치율(percentage DESC) 기준으로 정렬!
        results.sort(Comparator.comparing((FridgeMatchResult r) -> r.getRecipe().getScoreWeight(), Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(FridgeMatchResult::getMatchPercentage, Comparator.reverseOrder()));

        return results;
    }
}
