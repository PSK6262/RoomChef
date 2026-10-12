package com.app.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [API 응답 코드 및 표준 메시지 관리]
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {
    // 성공 계열
    SUCCESS("SUC_001", "성공"),
    SUC_EMPTY("SUC_002", "조회 결과가 없습니다."),

    // 기본 실패 계열
    FAIL("REJ_001", "요청 처리 중 오류가 발생했습니다."),
    INVALID_INPUT("REJ_002", "요청값을 확인해주세요."),
    UNAUTHORIZED("REJ_003", "로그인이 필요한 서비스입니다."),
    FORBIDDEN("REJ_004", "접근 권한이 없습니다."),

    // 회원 / 인증 영역
    USER_NOT_FOUND("REJ_101", "사용자 정보를 찾을 수 없습니다."),
    DUPLICATE_LOGIN_ID("REJ_102", "이미 사용 중인 아이디입니다."),
    DUPLICATE_NICKNAME("REJ_103", "이미 사용 중인 닉네임입니다."),
    DUPLICATE_EMAIL("REJ_104", "이미 등록된 이메일입니다."),
    INVALID_PASSWORD("REJ_105", "비밀번호가 일치하지 않습니다."),

    // 레시피 & 냉장고 영역
    RECIPE_NOT_FOUND("REJ_201", "레시피 정보를 찾을 수 없습니다."),
    INGREDIENT_NOT_FOUND("REJ_202", "식재료 정보를 찾을 수 없습니다."),
    FRIDGE_EMPTY("REJ_203", "냉장고에 등록된 식재료가 없습니다."),
    
    // 평가 & 피드백 영역
    EVALUATION_DUPLICATE("REJ_301", "이미 평가를 등록한 레시피입니다."),
    EVALUATION_NOT_FOUND("REJ_302", "평가 내역을 찾을 수 없습니다."),

    // 외부 API 및 AI 챗봇 영역
    EXTERNAL_API_ERROR("REJ_501", "외부 요리 데이터(API) 연동 중 오류가 발생했습니다."),
    GEMINI_API_ERROR("REJ_502", "AI 챗봇 처리 중 일시적인 오류가 발생했습니다.");

    private final String code;
    private final String message;
}
