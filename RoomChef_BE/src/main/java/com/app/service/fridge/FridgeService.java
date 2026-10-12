package com.app.service.fridge;

import java.time.LocalDate;
import java.util.List;
import com.app.dto.fridge.FridgeIngredients;
import com.app.dto.fridge.FridgeMatchResult;

public interface FridgeService {
    List<FridgeIngredients> getUserIngredients(Long userId);
    int addIngredient(Long userId, String ingredientName, String quantity, LocalDate expireDate);
    int removeIngredient(Long fridgeId, Long userId);
    int clearFridge(Long userId);
    List<FridgeMatchResult> matchUserFridgeRecipes(Long userId, boolean allowPartial);
}
