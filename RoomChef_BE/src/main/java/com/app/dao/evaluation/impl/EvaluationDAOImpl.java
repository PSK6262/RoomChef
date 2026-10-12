package com.app.dao.evaluation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dao.evaluation.EvaluationDAO;
import com.app.dto.evaluation.RecipeEvaluations;

@Repository
public class EvaluationDAOImpl implements EvaluationDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Override
    public int insertOrUpdateEvaluation(RecipeEvaluations evaluation) {
        return sqlSession.insert("EvaluationMapper.insertOrUpdateEvaluation", evaluation);
    }

    @Override
    public RecipeEvaluations selectEvaluationByUserAndRecipe(Long userId, Long recipeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("recipeId", recipeId);
        return sqlSession.selectOne("EvaluationMapper.selectEvaluationByUserAndRecipe", params);
    }

    @Override
    public List<RecipeEvaluations> selectEvaluationsByRecipeId(Long recipeId) {
        return sqlSession.selectList("EvaluationMapper.selectEvaluationsByRecipeId", recipeId);
    }

    @Override
    public List<Map<String, Object>> selectRecipeAvgRatings() {
        return sqlSession.selectList("EvaluationMapper.selectRecipeAvgRatings");
    }

    @Override
    public int updateRecipeScoreWeight(Long recipeId, Double scoreWeight) {
        Map<String, Object> params = new HashMap<>();
        params.put("recipeId", recipeId);
        params.put("scoreWeight", scoreWeight);
        return sqlSession.update("EvaluationMapper.updateRecipeScoreWeight", params);
    }
}
