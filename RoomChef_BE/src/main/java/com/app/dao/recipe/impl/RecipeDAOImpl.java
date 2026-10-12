package com.app.dao.recipe.impl;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.dao.recipe.RecipeDAO;
import com.app.dto.recipe.RecipeIngredients;
import com.app.dto.recipe.RecipeSteps;
import com.app.dto.recipe.Recipes;

@Repository
public class RecipeDAOImpl implements RecipeDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Override
    public List<Recipes> selectRecipeList(Map<String, Object> params) {
        return sqlSession.selectList("RecipeMapper.selectRecipeList", params);
    }

    @Override
    public Recipes selectRecipeById(Long recipeId) {
        return sqlSession.selectOne("RecipeMapper.selectRecipeById", recipeId);
    }

    @Override
    public List<RecipeIngredients> selectRecipeIngredients(Long recipeId) {
        return sqlSession.selectList("RecipeMapper.selectRecipeIngredients", recipeId);
    }

    @Override
    public List<RecipeSteps> selectRecipeSteps(Long recipeId) {
        return sqlSession.selectList("RecipeMapper.selectRecipeSteps", recipeId);
    }

    @Override
    public int insertRecipe(Recipes recipe) {
        return sqlSession.insert("RecipeMapper.insertRecipe", recipe);
    }

    @Override
    public int insertRecipeIngredient(RecipeIngredients ingredient) {
        return sqlSession.insert("RecipeMapper.insertRecipeIngredient", ingredient);
    }

    @Override
    public int insertRecipeStep(RecipeSteps step) {
        return sqlSession.insert("RecipeMapper.insertRecipeStep", step);
    }

    @Override
    public int incrementViewCount(Long recipeId) {
        return sqlSession.update("RecipeMapper.incrementViewCount", recipeId);
    }

    @Override
    public List<Recipes> selectAllRecipesWithIngredients() {
        return sqlSession.selectList("RecipeMapper.selectAllRecipesWithIngredients");
    }
}
