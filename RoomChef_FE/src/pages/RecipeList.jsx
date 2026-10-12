import React, { useState, useEffect } from 'react'
import { api } from '../utils/api'
import RecipeCard from '../components/RecipeCard'

export default function RecipeList({ onSelectRecipe, onRateRecipe, onStartCooking }) {
  const [recipes, setRecipes] = useState([])
  const [loading, setLoading] = useState(false)
  const [keyword, setKeyword] = useState('')
  const [sourceType, setSourceType] = useState('')
  const [onePanOnly, setOnePanOnly] = useState(false)

  // 기본 더미 데이터 (DB 미연결 시에도 즉시 UI 동작을 확인할 수 있도록 폴백 데이터 탑재)
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
      scoreWeight: 1.4,
      avgRating: 4.8,
      evalCount: 15,
      thumbnailUrl: '',
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
      scoreWeight: 1.2,
      avgRating: 4.5,
      evalCount: 22,
      thumbnailUrl: '',
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
      scoreWeight: 1.0,
      avgRating: 3.5,
      evalCount: 9,
      thumbnailUrl: '',
    },
    {
      recipeId: 4,
      title: '순두부 열라면 뚝배기 찌개',
      sourceType: 'PUBLIC_API',
      channelName: '식품안전나라 레시피',
      cookTimeMin: 12,
      difficulty: 'EASY',
      servings: 1,
      onePanYn: 'Y',
      microwaveYn: 'N',
      scoreWeight: 0.8,
      avgRating: 2.1,
      evalCount: 4,
      thumbnailUrl: '',
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
        setRecipes(dummyRecipes)
      }
    } catch {
      setRecipes(dummyRecipes)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadRecipes()
  }, [sourceType, onePanOnly])

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    loadRecipes()
  }

  return (
    <div className="page-container">
      <div className="hero-banner">
        <h2>혼자서도 잘 먹는 방구석 자취 식단 🍳</h2>
        <p>유튜브 실전 레시피와 공공데이터를 모아, 내 냉장고 재료로 뚝딱 만듭니다.</p>

        <form className="search-bar" onSubmit={handleSearchSubmit}>
          <input
            type="text"
            placeholder="요리 이름, 채널명(자취요리신, 뚝딱이형)으로 검색..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button type="submit">검색</button>
        </form>

        <div className="filter-chips">
          <button
            className={`chip ${sourceType === '' ? 'active' : ''}`}
            onClick={() => setSourceType('')}
          >
            전체 보기
          </button>
          <button
            className={`chip ${sourceType === 'YOUTUBE' ? 'active' : ''}`}
            onClick={() => setSourceType('YOUTUBE')}
          >
            🎬 유튜브 레시피
          </button>
          <button
            className={`chip ${sourceType === 'PUBLIC_API' ? 'active' : ''}`}
            onClick={() => setSourceType('PUBLIC_API')}
          >
            🏛️ 공공데이터 레시피
          </button>
          <button
            className={`chip ${onePanOnly ? 'active' : ''}`}
            onClick={() => setOnePanOnly(!onePanOnly)}
          >
            🍳 원팬(설거지 최소) 요리만
          </button>
        </div>
      </div>

      <div className="recipe-grid-header">
        <h3>추천 레시피 ({recipes.length}개)</h3>
        <span className="sort-hint">⭐ AI 추천 가산점 순으로 자동 정렬됩니다.</span>
      </div>

      {loading ? (
        <div className="loading-spinner">레시피를 불러오는 중입니다...</div>
      ) : (
        <div className="recipe-grid">
          {recipes.map((recipe) => (
            <RecipeCard
              key={recipe.recipeId}
              recipe={recipe}
              onSelect={onSelectRecipe}
              onRate={onRateRecipe}
              onStartCooking={onStartCooking}
            />
          ))}
        </div>
      )}
    </div>
  )
}
