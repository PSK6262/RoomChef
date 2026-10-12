package com.app.service.evaluation.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.evaluation.EvaluationDAO;
import com.app.dto.evaluation.RecipeEvaluations;
import com.app.service.evaluation.EvaluationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationDAO evaluationDAO;

    @Override
    @Transactional
    public int submitEvaluation(Long userId, Long recipeId, Double rating, String feedbackNote) {
        RecipeEvaluations eval = new RecipeEvaluations();
        eval.setUserId(userId);
        eval.setRecipeId(recipeId);
        eval.setRating(rating);
        eval.setFeedbackNote(feedbackNote);

        int result = evaluationDAO.insertOrUpdateEvaluation(eval);

        // 즉시 가중치 반영 로직 (실시간 반영 + 일일 스케줄러 보정)
        if (rating >= 4.0) {
            evaluationDAO.updateRecipeScoreWeight(recipeId, 1.3); // 가산점
        } else if (rating <= 2.0) {
            evaluationDAO.updateRecipeScoreWeight(recipeId, 0.7); // 감점
        } else {
            evaluationDAO.updateRecipeScoreWeight(recipeId, 1.0); // 3점 유지
        }

        return result;
    }

    @Override
    public RecipeEvaluations getMyEvaluation(Long userId, Long recipeId) {
        return evaluationDAO.selectEvaluationByUserAndRecipe(userId, recipeId);
    }

    @Override
    public List<RecipeEvaluations> getRecipeEvaluations(Long recipeId) {
        return evaluationDAO.selectEvaluationsByRecipeId(recipeId);
    }

    /**
     * 일일 스케줄러에서 호출: 모든 레시피의 평균 평점을 집계하여 AI 추천 노출 가중치(SCORE_WEIGHT) 일괄 갱신
     */
    @Override
    @Transactional
    public int recalculateAllRecipeWeights() {
        List<Map<String, Object>> avgList = evaluationDAO.selectRecipeAvgRatings();
        if (avgList == null || avgList.isEmpty()) {
            log.info("[EvaluationService] 갱신할 평가 내역이 없습니다.");
            return 0;
        }

        int updatedCount = 0;
        for (Map<String, Object> item : avgList) {
            Number recipeIdNum = (Number) item.get("RECIPE_ID");
            Number avgRatingNum = (Number) item.get("AVG_RATING");
            if (recipeIdNum == null || avgRatingNum == null) continue;

            long recipeId = recipeIdNum.longValue();
            double avgRating = avgRatingNum.doubleValue();

            double calculatedWeight = 1.0;
            if (avgRating >= 4.0) {
                // 4점 이상: 가산점 (예: 4.5점 -> 1.0 + 0.75 = 1.75)
                calculatedWeight = 1.0 + ((avgRating - 3.0) * 0.5);
            } else if (avgRating <= 2.0) {
                // 2점 이하: 감점 (최저 0.2)
                calculatedWeight = Math.max(0.2, 1.0 - ((3.0 - avgRating) * 0.4));
            } else {
                // 3점 대: 1.0 유지
                calculatedWeight = 1.0;
            }

            calculatedWeight = Math.round(calculatedWeight * 100.0) / 100.0;
            evaluationDAO.updateRecipeScoreWeight(recipeId, calculatedWeight);
            updatedCount++;
        }

        return updatedCount;
    }
}
