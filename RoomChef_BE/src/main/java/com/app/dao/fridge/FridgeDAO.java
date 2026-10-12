package com.app.dao.fridge;

import java.util.List;
import com.app.dto.fridge.FridgeIngredients;

public interface FridgeDAO {
    List<FridgeIngredients> selectIngredientsByUserId(Long userId);
    int insertIngredient(FridgeIngredients item);
    int deleteIngredient(Long fridgeId, Long userId);
    int deleteAllIngredients(Long userId);
}
