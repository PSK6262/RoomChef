# 🍳 방구석 셰프 (RoomChef)
### 1인 가구 자취생을 위한 스마트 AI 자취요리 & 냉장고 파먹기 플랫폼

> **서비스명**: 방구석 셰프 (RoomChef)  
> **GitHub**: [https://github.com/PSK6262/RoomChef.git](https://github.com/PSK6262/RoomChef.git)  
> "배달비 아끼고, 냉장고 털고! 1인분 계량과 AI 알고리즘으로 완성하는 나만의 자취 식단"

---

## 💡 서비스 소개
**방구석 셰프 (RoomChef)**는 재료가 남아 썩거나, 복잡한 계량 때문에 요리를 망설이는 자취생들을 위한 올인원 자취요리 웹 플랫폼입니다.  
유튜브 인기 크리에이터(자취요리신, 뚝딱이형 등)의 실전 레시피와 국가 공공데이터 레시피를 수집·정제하고, 내 냉장고 속 재료로 만들 수 있는 요리를 AI가 분석하여 추천합니다.

---

## ✨ 핵심 기능

### 1. 🎬 유튜브 & 공공데이터 기반 자취 레시피 수집
* **자취 전문 유튜브 채널**의 검증된 초간단 레시피와 **공공데이터포털/식품안전나라** 레시피 DB 연동.
* 원팬(설거지 최소화), 전자레인지 전용, 조리시간 10분 컷 등 자취 친화형 필터링 제공.

### 2. 🧊 냉장고 파먹기 (완전 일치 vs 부분 일치 매칭)
* **완전 가능 요리 (100% 일치)**: 장보지 않고 지금 있는 재료로 바로 시작할 수 있는 요리.
* **부분 가능 요리 (부족 재료 1~2개)**: 몇 가지만 더 사면 만들 수 있는 요리 + **식재료 대체 꿀팁** 안내 (예: 굴소스 $\rightarrow$ 간장+설탕+다시다).
* **자취 기본 양념 분리**: 진간장, 식용유, 설탕, 소금, 고춧가루, 라면스프 등은 기본 구비 품목으로 자동 분류.

### 3. ⭐ 실전 평가 & AI 알고리즘 피드백 루프
* 요리를 직접 만들어본 후 1~5점 평점 등록:
  * **4~5점 (호평)**: AI 추천 알고리즘에서 해당 레시피 및 유사 요리에 **가산점(+0.3)** 부여 $\rightarrow$ 더 자주 노출.
  * **1~2점 (혹평)**: 사용자 취향을 반영하여 **감점(-0.3)** 부여 $\rightarrow$ 노출 빈도 하향.
  * **3점 (보통)**: 기존 가중치 유지.
* **일일 스케줄러 (`RecipeWeightScheduler`)**: 매일 자정 사용자 평가를 집계하여 추천 가중치 일괄 재계산 및 선호도 업데이트.

### 4. 🤖 방구석 셰프 AI 챗봇
* Google Gemini API 기반의 자취 특화 요리 비서.
* "스팸이랑 양파로 뭐 할 수 있어?", "월급 전날 3,000원 컷 요리", "숙취 해소용 해장 요리" 등 상황별 맞춤 추천.

### 5. 👨‍🍳 핸즈프리 조리 모드 & 1인분 자동 계량
* 손에 기름이나 물이 묻은 상황을 고려한 **큰 글씨 단계별 조리 카드 UI**.
* 조리 시간 언급 시 버튼 한 번으로 실행되는 **빌트인 타이머**.
* 밥숟가락/종이컵 기준의 **1인분 정밀 계량 환산**.

---

## 🛠 아키텍처 및 기술 스택 (BuildUp 아키텍처 계승)

```
RoomChef/
├── RoomChef_BE/                # 백엔드 (Spring MVC, MyBatis, Gemini AI)
│   ├── src/main/java/com/app/
│   │   ├── common/             # ApiResponse, ResultCode, CommonCode
│   │   ├── controller/         # Auth, Recipe, Fridge, Evaluation, Chatbot, User
│   │   ├── dao/                # MyBatis DAO 인터페이스 및 구현체
│   │   ├── dto/                # Recipes, FridgeIngredients, Evaluations 등
│   │   ├── interceptor/        # JWT / 로그인 인증 인터셉터
│   │   ├── scheduler/          # 매일 자정 평점 가중치 재계산 스케줄러
│   │   ├── service/            # 비즈니스 로직
│   │   └── util/               # JwtProvider, ApiBridgeUtil, GeminiApiClient
│   ├── src/main/resources/
│   │   ├── config/             # application, db, smtp properties
│   │   ├── mybatis/            # SQL Mapper XML (Recipe, Fridge, Eval, User)
│   │   └── sql/                # RoomChef_init.sql DDL 스크립트
│   └── pom.xml
│
└── RoomChef_FE/                # 프론트엔드 (React 19, Vite, Redux Toolkit)
    ├── src/
    │   ├── components/         # Navbar, RecipeCard, ChatbotModal, CookingModeModal
    │   ├── pages/              # RecipeList, RecipeDetail, Fridge, Login
    │   ├── store/              # Redux Toolkit (authSlice, recipeSlice, fridgeSlice)
    │   └── utils/              # api.js, formatUtils.js
    ├── package.json
    └── vite.config.js
```

* **Frontend**: React 19, Vite, Redux Toolkit
* **Backend**: Java 17, Spring 5.3 MVC, MyBatis 3.5, Commons DBCP2, JJWT
* **Database**: Oracle Database / 호환 SQL (DDL 스키마 완비)
* **AI & External APIs**: Google Gemini API, YouTube Data API, 공공데이터포털(식품안전나라)
