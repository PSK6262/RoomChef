package com.app.common;

/**
 * [RoomChef 공통 상수 인터페이스]
 */
public interface CommonCode {
    // 1. 회원 권한 등급
    public static final Long ROLE_USER = 1L;          // 일반 자취생 회원
    public static final Long ROLE_ADMIN = 9L;         // 최고 관리자

    // 2. 세션 키
    public static final String SESSION_LOGIN_USER = "loginUser";

    // 3. 레시피 출처 타입
    public static final String SOURCE_YOUTUBE = "YOUTUBE";
    public static final String SOURCE_PUBLIC_API = "PUBLIC_API";
    public static final String SOURCE_USER = "USER_CUSTOM";

    // 4. 평가 가중치 기준
    public static final double WEIGHT_BONUS = 0.3;    // 4~5점일 때 추천 노출 가산치
    public static final double WEIGHT_PENALTY = -0.3; // 1~2점일 때 추천 노출 감점치
    public static final double WEIGHT_DEFAULT = 1.0;  // 3점(기본 유지)
}
