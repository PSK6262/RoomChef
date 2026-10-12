import React, { useState, useEffect } from 'react'
import { api } from '../utils/api'
import RecipeCard from '../components/RecipeCard'

export default function RecipeList({ onSelectRecipe, onRateRecipe, onStartCooking }) {
  const [recipes, setRecipes] = useState([])
  const [visibleCount, setVisibleCount] = useState(6)
  const [loading, setLoading] = useState(false)
  const [keyword, setKeyword] = useState('')
  const [sourceType, setSourceType] = useState('')
  const [onePanOnly, setOnePanOnly] = useState(false)

  // 20개의 현실감 넘치는 자취 요리 예시 데이터
  const dummyRecipes = [
    {
      recipeId: 1,
      title: '원팬 5분 스팸 김치볶음밥',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 10,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.5,
      avgRating: 4.8,
      evalCount: 28,
    },
    {
      recipeId: 2,
      title: '전자레인지 초간단 계란찜',
      sourceType: 'YOUTUBE',
      channelName: '뚝딱이형',
      cookTimeMin: 5,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'N',
      microwaveYn: 'Y',
      scoreWeight: 1.4,
      avgRating: 4.6,
      evalCount: 34,
    },
    {
      recipeId: 3,
      title: '대파 듬뿍 진간장 볶음라면',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 8,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.3,
      avgRating: 4.4,
      evalCount: 19,
    },
    {
      recipeId: 4,
      title: '고추참치 마요 비빔밥',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 3,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.25,
      avgRating: 4.5,
      evalCount: 15,
    },
    {
      recipeId: 5,
      title: '순두부 열라면 뚝배기 찌개',
      sourceType: 'PUBLIC_API',
      channelName: '식품안전나라',
      cookTimeMin: 12,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.2,
      avgRating: 4.3,
      evalCount: 22,
    },
    {
      recipeId: 6,
      title: '원팬 마늘 베이컨 오일 파스타',
      sourceType: 'YOUTUBE',
      channelName: '1분요리 뚝딱이형',
      cookTimeMin: 12,
      difficulty: 'MEDIUM',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.15,
      avgRating: 4.2,
      evalCount: 11,
    },
    {
      recipeId: 7,
      title: '스팸 감자짜글이 찌개',
      sourceType: 'YOUTUBE',
      channelName: '백종원의 요리비책',
      cookTimeMin: 20,
      difficulty: 'MEDIUM',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.1,
      avgRating: 4.5,
      evalCount: 42,
    },
    {
      recipeId: 8,
      title: '기름떡볶이 5분 컷',
      sourceType: 'YOUTUBE',
      channelName: '뚝딱이형',
      cookTimeMin: 5,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.05,
      avgRating: 3.9,
      evalCount: 14,
    },
    {
      recipeId: 9,
      title: '팽이버섯 불닭 소스 볶음',
      sourceType: 'YOUTUBE',
      channelName: '쿠킹하루',
      cookTimeMin: 7,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.0,
      avgRating: 3.8,
      evalCount: 8,
    },
    {
      recipeId: 10,
      title: '진미채 버터구이 안주',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 5,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 1.0,
      avgRating: 3.7,
      evalCount: 12,
    },
    {
      recipeId: 11,
      title: '훈제오리 부추 굴소스 볶음',
      sourceType: 'PUBLIC_API',
      channelName: '식품안전나라',
      cookTimeMin: 10,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.95,
      avgRating: 3.6,
      evalCount: 16,
    },
    {
      recipeId: 12,
      title: '대파 슬라이스 치즈 토스트',
      sourceType: 'YOUTUBE',
      channelName: '뚝딱이형',
      cookTimeMin: 6,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'N',
      microwaveYn: 'Y',
      scoreWeight: 0.95,
      avgRating: 3.5,
      evalCount: 7,
    },
    {
      recipeId: 13,
      title: '참치 쌈장 비빔 덮밥',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 5,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.9,
      avgRating: 3.4,
      evalCount: 18,
    },
    {
      recipeId: 14,
      title: '김치 참치 얼큰 찌개',
      sourceType: 'YOUTUBE',
      channelName: '백종원의 요리비책',
      cookTimeMin: 15,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.9,
      avgRating: 3.5,
      evalCount: 30,
    },
    {
      recipeId: 15,
      title: '소시지 야채 케첩 볶음',
      sourceType: 'PUBLIC_API',
      channelName: '식품안전나라',
      cookTimeMin: 10,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.85,
      avgRating: 3.2,
      evalCount: 14,
    },
    {
      recipeId: 16,
      title: '명란 마요 감태 삼각 비빔밥',
      sourceType: 'YOUTUBE',
      channelName: '쿠킹하루',
      cookTimeMin: 6,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'N',
      microwaveYn: 'N',
      scoreWeight: 0.85,
      avgRating: 3.1,
      evalCount: 5,
    },
    {
      recipeId: 17,
      title: '대패 삼겹살 숙주 볶음',
      sourceType: 'YOUTUBE',
      channelName: '뚝딱이형',
      cookTimeMin: 12,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.8,
      avgRating: 3.0,
      evalCount: 17,
    },
    {
      recipeId: 18,
      title: '전자레인지 초간단 콘치즈',
      sourceType: 'YOUTUBE',
      channelName: '1분요리 뚝딱이형',
      cookTimeMin: 4,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'N',
      microwaveYn: 'Y',
      scoreWeight: 0.75,
      avgRating: 2.8,
      evalCount: 9,
    },
    {
      recipeId: 19,
      title: '반숙 버터 간장 계란밥',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 5,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.7,
      avgRating: 2.5,
      evalCount: 13,
    },
    {
      recipeId: 20,
      title: '멸치 칼국수 국물 라볶이',
      sourceType: 'YOUTUBE',
      channelName: '자취요리신',
      cookTimeMin: 10,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.65,
      avgRating: 2.2,
      evalCount: 6,
    },
  ]

  const loadRecipes = async () => {
    setLoading(true)
    try {
      const data = await api.getRecipes({
        keyword,
        sourceType,
        onePanYn: onePanOnly ? 'Y' : '',
      })
      if (data && data.length > 0) {
        setRecipes(data)
      } else {
        filterDummyData()
      }
    } catch {
      filterDummyData()
    } finally {
      setLoading(false)
    }
  }

  const filterDummyData = () => {
    let list = [...dummyRecipes]
    if (keyword.trim()) {
      list = list.filter(
        (r) =>
          r.title.includes(keyword.trim()) || r.channelName.includes(keyword.trim())
      )
    }
    if (sourceType) {
      list = list.filter((r) => r.sourceType === sourceType)
    }
    if (onePanOnly) {
      list = list.filter((r) => r.onePanYn === 'Y')
    }
    setRecipes(list)
    setVisibleCount(6) // 필터 변경 시 6개로 초기화
  }

  useEffect(() => {
    loadRecipes()
  }, [sourceType, onePanOnly])

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    filterDummyData()
  }

  const handleLoadMore = () => {
    setVisibleCount((prev) => prev + 3)
  }

  const currentVisibleRecipes = recipes.slice(0, visibleCount)

  return (
    <div className="page-container">
      <div className="hero-banner">
        <h2>방구석 쉐프 - 자취요리 플랫폼</h2>

        <form className="search-bar" onSubmit={handleSearchSubmit}>
          <input
            type="text"
            placeholder="요리 이름, 채널명(자취요리신, 뚝딱이형)으로 검색"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button type="submit">검색</button>
        </form>

        <div className="filter-chips">
          <button
            className={`chip ${sourceType === '' && !onePanOnly ? 'active' : ''}`}
            onClick={() => {
              setSourceType('')
              setOnePanOnly(false)
            }}
          >
            전체 보기
          </button>
          <button
            className={`chip ${sourceType === 'YOUTUBE' ? 'active' : ''}`}
            onClick={() => setSourceType('YOUTUBE')}
          >
            유튜브 레시피
          </button>
          <button
            className={`chip ${sourceType === 'PUBLIC_API' ? 'active' : ''}`}
            onClick={() => setSourceType('PUBLIC_API')}
          >
            공공데이터 레시피
          </button>
          <button
            className={`chip ${onePanOnly ? 'active' : ''}`}
            onClick={() => setOnePanOnly(!onePanOnly)}
          >
            원팬 요리만
          </button>
        </div>
      </div>

      <div className="recipe-grid-header">
        <h3>추천 레시피 ({recipes.length}개)</h3>
        <span className="sort-hint">AI 추천 가산점 순으로 자동 정렬됩니다.</span>
      </div>

      {loading ? (
        <div className="loading-spinner">레시피를 불러오는 중입니다...</div>
      ) : (
        <>
          <div className="recipe-grid">
            {currentVisibleRecipes.map((recipe) => (
              <RecipeCard
                key={recipe.recipeId}
                recipe={recipe}
                onSelect={onSelectRecipe}
                onRate={onRateRecipe}
                onStartCooking={onStartCooking}
              />
            ))}
          </div>

          {visibleCount < recipes.length && (
            <div className="load-more-container">
              <button className="btn-load-more" onClick={handleLoadMore}>
                더보기 ({visibleCount}/{recipes.length})
              </button>
            </div>
          )}
        </>
      )}
    </div>
  )
}
