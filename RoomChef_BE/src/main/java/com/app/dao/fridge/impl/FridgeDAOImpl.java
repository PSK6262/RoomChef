package com.app.dao.fridge.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dao.fridge.FridgeDAO;
import com.app.dto.fridge.FridgeIngredients;

@Repository
public class FridgeDAOImpl implements FridgeDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Override
    public List<FridgeIngredients> selectIngredientsByUserId(Long userId) {
        return sqlSession.selectList("FridgeMapper.selectIngredientsByUserId", userId);
    }

    @Override
    public int insertIngredient(FridgeIngredients item) {
        return sqlSession.insert("FridgeMapper.insertIngredient", item);
    }

    @Override
    public int deleteIngredient(Long fridgeId, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("fridgeId", fridgeId);
        params.put("userId", userId);
        return sqlSession.delete("FridgeMapper.deleteIngredient", params);
    }

    @Override
    public int deleteAllIngredients(Long userId) {
        return sqlSession.delete("FridgeMapper.deleteAllIngredients", userId);
    }
}
