package com.app.controller.evaluation;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.ApiResponse;
import com.app.common.CommonCode;
import com.app.common.ResultCode;
import com.app.dto.evaluation.RecipeEvaluations;
import com.app.dto.user.Users;
import com.app.service.evaluation.EvaluationService;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    private Long getUserId(HttpServletRequest request) {
        Users user = (Users) request.getAttribute(CommonCode.SESSION_LOGIN_USER);
        return (user != null) ? user.getUserId() : null;
    }

    /**
     * 레시피 평가 등록 (1~5점 평점 및 피드백)
     * - 4점 이상: AI 가중치 가산
     * - 2점 이하: AI 가중치 감점
     * - 3점: 기본 유지
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> submitEvaluation(
            @RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        Number recipeIdNum = (Number) body.get("recipeId");
        Number ratingNum = (Number) body.get("rating");
        String feedback = (String) body.get("feedbackNote");

        if (recipeIdNum == null || ratingNum == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ResultCode.INVALID_INPUT));
        }

        double rating = ratingNum.doubleValue();
        evaluationService.submitEvaluation(userId, recipeIdNum.longValue(), rating, feedback);

        String msg = (rating >= 4.0) 
                ? "평가 등록 완료! 호평 레시피로 AI 추천 알고리즘에 가산점이 반영되었습니다."
                : (rating <= 2.0)
                ? "평가 등록 완료! 취향을 고려하여 해당 요리 계열의 AI 추천 노출이 하향 조정됩니다."
                : "평가 등록 완료! 기본 가중치가 유지됩니다.";

        return ResponseEntity.ok(ApiResponse.success(msg));
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<List<RecipeEvaluations>>> getRecipeEvaluations(@PathVariable Long recipeId) {
        List<RecipeEvaluations> list = evaluationService.getRecipeEvaluations(recipeId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/my/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeEvaluations>> getMyEvaluation(
            @PathVariable Long recipeId, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return ResponseEntity.status(401).body(ApiResponse.error(ResultCode.UNAUTHORIZED));

        RecipeEvaluations eval = evaluationService.getMyEvaluation(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success(eval));
    }
}
