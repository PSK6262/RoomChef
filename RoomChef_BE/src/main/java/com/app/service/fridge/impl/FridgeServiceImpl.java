package com.app.service.fridge.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.fridge.FridgeDAO;
import com.app.dto.fridge.FridgeIngredients;
import com.app.dto.fridge.FridgeMatchResult;
import com.app.service.fridge.FridgeService;
import com.app.service.recipe.RecipeService;

@Service
public class FridgeServiceImpl implements FridgeService {

    @Autowired
    private FridgeDAO fridgeDAO;

    @Autowired
    private RecipeService recipeService;

    @Override
    public List<FridgeIngredients> getUserIngredients(Long userId) {
        return fridgeDAO.selectIngredientsByUserId(userId);
    }

    @Override
    public int addIngredient(Long userId, String ingredientName, String quantity, LocalDate expireDate) {
        FridgeIngredients item = new FridgeIngredients();
        item.setUserId(userId);
        item.setIngredientName(ingredientName);
        item.setQuantity(quantity);
        item.setExpireDate(expireDate);
        return fridgeDAO.insertIngredient(item);
    }

    @Override
    public int removeIngredient(Long fridgeId, Long userId) {
        return fridgeDAO.deleteIngredient(fridgeId, userId);
    }

    @Override
    public int clearFridge(Long userId) {
        return fridgeDAO.deleteAllIngredients(userId);
    }

    @Override
    public List<FridgeMatchResult> matchUserFridgeRecipes(Long userId, boolean allowPartial) {
        List<FridgeIngredients> fridgeList = fridgeDAO.selectIngredientsByUserId(userId);
        List<String> ingredientNames = fridgeList.stream()
                .map(FridgeIngredients::getIngredientName)
                .collect(Collectors.toList());

        return recipeService.matchRecipesByIngredients(ingredientNames, allowPartial);
    }
}
