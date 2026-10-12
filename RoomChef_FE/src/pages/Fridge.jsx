import React, { useState, useEffect } from 'react'
import { api } from '../utils/api'
import RecipeCard from '../components/RecipeCard'

export default function Fridge({ onSelectRecipe, onRateRecipe, onStartCooking }) {
  const [ingredients, setIngredients] = useState([
    { fridgeId: 1, ingredientName: '스팸', quantity: '1캔' },
    { fridgeId: 2, ingredientName: '신김치', quantity: '반 통' },
    { fridgeId: 3, ingredientName: '계란', quantity: '3개' },
    { fridgeId: 4, ingredientName: '대파', quantity: '1대' },
  ])
  const [newIngName, setNewIngName] = useState('')
  const [newIngQty, setNewIngQty] = useState('')
  const [loading, setLoading] = useState(false)
  const [matchResults, setMatchResults] = useState([])
  const [hasSearched, setHasSearched] = useState(false)

  const loadIngredients = async () => {
    try {
      const data = await api.getFridgeIngredients()
      if (data && data.length > 0) {
        setIngredients(data)
      }
    } catch {
      // 비로그인 또는 에러 시 기본 유지
    }
  }

  useEffect(() => {
    loadIngredients()
  }, [])

  const handleAddIngredient = async (e) => {
    e.preventDefault()
    if (!newIngName.trim()) return

    const newItem = {
      fridgeId: Date.now(),
      ingredientName: newIngName.trim(),
      quantity: newIngQty.trim() || '적당량',
    }

    try {
      await api.addFridgeIngredient(newItem)
    } catch {
      // 로컬에 우선 추가
    }

    setIngredients([newItem, ...ingredients])
    setNewIngName('')
    setNewIngQty('')
  }

  const handleRemoveIngredient = async (id) => {
    try {
      await api.removeFridgeIngredient(id)
    } catch {
      // 로컬 처리
    }
    setIngredients(ingredients.filter((item) => item.fridgeId !== id))
  }

  const handleMatch = async () => {
    setLoading(true)
    setHasSearched(true)
    try {
      const ingNames = ingredients.map((i) => i.ingredientName)
      const data = await api.matchRecipes({ ingredients: ingNames, allowPartial: true })
      setMatchResults(data || [])
    } catch {
      // 폴백 매칭 결과
      setMatchResults([
        {
          recipe: {
            recipeId: 1,
            title: '원팬 5분 스팸 김치볶음밥',
            channelName: '자취요리신',
            cookTimeMin: 10,
            difficulty: 'EASY',
            scoreWeight: 1.4,
            avgRating: 4.8,
            onePanYn: 'Y',
          },
          fullyMatch: true,
          matchPercentage: 100,
          missingIngredientsCount: 0,
          matchedIngredients: ['스팸', '신김치', '진간장 (기본양념)'],
          missingIngredients: [],
          substituteTips: [],
        },
        {
          recipe: {
            recipeId: 3,
            title: '대파 듬뿍 진간장 볶음라면',
            channelName: '자취요리신',
            cookTimeMin: 8,
            difficulty: 'EASY',
            scoreWeight: 1.0,
            avgRating: 3.5,
            onePanYn: 'Y',
          },
          fullyMatch: false,
          matchPercentage: 70,
          missingIngredientsCount: 1,
          matchedIngredients: ['대파', '계란', '진간장 (기본양념)'],
          missingIngredients: ['라면사리'],
          substituteTips: ['라면사리가 없으면: 소면이나 우동사리로 대체 가능'],
        },
      ])
    } finally {
      setLoading(false)
    }
  }

  const fullyMatches = matchResults.filter((r) => r.fullyMatch)
  const partialMatches = matchResults.filter((r) => !r.fullyMatch)

  return (
    <div className="page-container">
      <div className="fridge-hero">
        <h2>🧊 내 냉장고 속 식재료 관리</h2>
        <p>남아있는 재료를 등록해두면, 장보지 않고 바로 만들 수 있는 요리를 찾아줍니다.</p>

        <form className="fridge-add-bar" onSubmit={handleAddIngredient}>
          <input
            type="text"
            placeholder="재료명 (예: 양파, 두부, 참치캔)"
            value={newIngName}
            onChange={(e) => setNewIngName(e.target.value)}
          />
          <input
            type="text"
            placeholder="수량 (예: 반 개, 1캔)"
            value={newIngQty}
            onChange={(e) => setNewIngQty(e.target.value)}
          />
          <button type="submit">+ 냉장고에 넣기</button>
        </form>

        <div className="fridge-chips">
          {ingredients.map((item) => (
            <div key={item.fridgeId} className="fridge-chip">
              <span className="chip-name">{item.ingredientName}</span>
              <span className="chip-qty">({item.quantity})</span>
              <button
                type="button"
                className="chip-del"
                onClick={() => handleRemoveIngredient(item.fridgeId)}
              >
                ✕
              </button>
            </div>
          ))}
        </div>

        <button className="btn-match-run" onClick={handleMatch} disabled={loading || ingredients.length === 0}>
          {loading ? '알고리즘 분석 중...' : '🔍 지금 있는 재료로 만들 수 있는 요리 찾기'}
        </button>
      </div>

      {hasSearched && (
        <div className="match-results-area">
          <div className="match-section">
            <h3 className="section-title full">
              ✅ 바로 조리 가능! 완전 일치 요리 ({fullyMatches.length}개)
            </h3>
            {fullyMatches.length === 0 ? (
              <p className="empty-hint">모든 재료가 100% 일치하는 요리가 없습니다. 아래 부분 일치 요리를 확인해보세요.</p>
            ) : (
              <div className="recipe-grid">
                {fullyMatches.map((m) => (
                  <RecipeCard
                    key={m.recipe.recipeId}
                    recipe={m.recipe}
                    onSelect={onSelectRecipe}
                    onRate={onRateRecipe}
                    onStartCooking={onStartCooking}
                  />
                ))}
              </div>
            )}
          </div>

          <div className="match-section">
            <h3 className="section-title partial">
              🛒 1~2개만 더 사면 가능! 부분 일치 요리 ({partialMatches.length}개)
            </h3>
            {partialMatches.length === 0 ? (
              <p className="empty-hint">추천할 부분 일치 요리가 없습니다.</p>
            ) : (
              <div className="recipe-grid">
                {partialMatches.map((m) => (
                  <div key={m.recipe.recipeId} className="partial-card-wrap">
                    <RecipeCard
                      recipe={m.recipe}
                      onSelect={onSelectRecipe}
                      onRate={onRateRecipe}
                      onStartCooking={onStartCooking}
                    />
                    <div className="missing-summary">
                      <span className="missing-label">부족한 재료:</span>
                      <span className="missing-items">{m.missingIngredients.join(', ')}</span>
                      {m.substituteTips?.length > 0 && (
                        <div className="sub-tip-box">
                          💡 <strong>대체 팁:</strong> {m.substituteTips[0]}
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
