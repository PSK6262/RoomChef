package com.app.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.service.evaluation.EvaluationService;
import lombok.extern.slf4j.Slf4j;

/**
 * [자취요리 AI 추천 가중치 일일 재학습/재계산 스케줄러 - RecipeWeightScheduler]
 * 
 * - 매일 자정(00:00:00) 1일 1회 실행
 * - 사용자의 레시피 실사용 평점(1~5점)을 집계하여 AI 추천 노출 가중치(SCORE_WEIGHT)를 자동 갱신:
 *   * 4.0점 이상 -> 가산점 부여 (+0.3) -> 검색/추천 알고리즘에서 최우선 노출
 *   * 2.0점 이하 -> 감점 부여 (-0.3) -> 검색/추천 알고리즘에서 노출 빈도 하향
 *   * 3.0점 -> 기본 가중치 유지 (1.0)
 */
@Slf4j
@Component
public class RecipeWeightScheduler {

    @Autowired(required = false)
    private EvaluationService evaluationService;

    // 매일 자정 0시 0분 0초에 자동 실행 (0 0 0 * * ?)
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleDailyRecipeWeightRecalculation() {
        log.info("[RecipeWeightScheduler] === 자취요리 AI 추천 가중치 일일 재계산 시작 ===");
        try {
            if (evaluationService != null) {
                int updatedCount = evaluationService.recalculateAllRecipeWeights();
                log.info("[RecipeWeightScheduler] 가중치 갱신 완료: 총 {}개 레시피 반영", updatedCount);
            } else {
                log.warn("[RecipeWeightScheduler] EvaluationService가 주입되지 않아 스킵되었습니다.");
            }
        } catch (Exception e) {
            log.error("[RecipeWeightScheduler] 가중치 갱신 중 오류 발생: {}", e.getMessage(), e);
        }
        log.info("[RecipeWeightScheduler] === 자취요리 AI 추천 가중치 일일 재계산 종료 ===");
    }
}
