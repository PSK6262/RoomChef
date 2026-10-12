import React from 'react'
import { formatDifficulty, formatTime, formatRating } from '../utils/formatUtils'

export default function RecipeDetail({ recipe, onBack, onRate, onStartCooking }) {
  const diff = formatDifficulty(recipe.difficulty)

  const defaultIngredients = recipe.ingredients || [
    { ingredientName: '스팸', amount: '1/2캔', isEssential: 'Y', isBasicSauce: 'N' },
    { ingredientName: '신김치', amount: '종이컵 1컵', isEssential: 'Y', isBasicSauce: 'N' },
    { ingredientName: '밥', amount: '1공기', isEssential: 'Y', isBasicSauce: 'N' },
    { ingredientName: '진간장', amount: '1스푼', isEssential: 'N', isBasicSauce: 'Y' },
    { ingredientName: '참기름', amount: '1스푼', isEssential: 'N', isBasicSauce: 'Y' },
  ]

  const defaultSteps = recipe.steps || [
    { stepOrder: 1, instruction: '스팸을 숟가락으로 으깨어 팬에 노릇하게 볶습니다.', timerSeconds: 120, tip: '기름 없이 스팸 기름으로 볶으세요.' },
    { stepOrder: 2, instruction: '신김치와 진간장 1스푼을 넣고 센불에 빠르게 볶습니다.', timerSeconds: 90, tip: '간장을 팬 가장자리에 둘러 태우듯 볶으면 불맛 UP!' },
    { stepOrder: 3, instruction: '밥을 넣고 주걱을 세워 섞은 뒤, 불을 끄고 참기름을 두릅니다.', timerSeconds: 60, tip: '김가루나 계란후라이를 얹으면 완성!' },
  ]

  return (
    <div className="page-container detail-page">
      <button className="btn-back" onClick={onBack}>
        ◀ 목록으로 돌아가기
      </button>

      <div className="detail-card">
        <div className="detail-header">
          <div className="detail-badge-group">
            <span className="badge" style={{ backgroundColor: diff.color }}>
              {diff.text}
            </span>
            {recipe.onePanYn === 'Y' && <span className="badge onepan">원팬 요리</span>}
            <span className="badge servings">👤 1인분 기준</span>
          </div>

          <h1 className="detail-title">{recipe.title}</h1>

          <div className="detail-meta">
            <span>📺 출처: {recipe.channelName || '유튜브'}</span>
            <span>⏱️ {formatTime(recipe.cookTimeMin)}</span>
            <span>★ 평점: {formatRating(recipe.avgRating)} ({recipe.evalCount || 0}명)</span>
          </div>
        </div>

        <div className="detail-actions-top">
          <button className="btn-primary-large" onClick={() => onStartCooking(recipe)}>
            👨‍🍳 조리 모드 시작 (타이머 & 큰 글씨)
          </button>
          <button className="btn-secondary-large" onClick={() => onRate(recipe)}>
            ⭐ 레시피 평가하기
          </button>
        </div>

        <div className="detail-content-grid">
          <div className="ingredients-section">
            <h3>🛒 준비 재료 (1인분 계량)</h3>
            <ul className="ing-list">
              {defaultIngredients.map((ing, idx) => (
                <li key={idx} className={ing.isBasicSauce === 'Y' ? 'basic-sauce' : ''}>
                  <span className="ing-name">
                    {ing.ingredientName}
                    {ing.isBasicSauce === 'Y' && <span className="tag-sauce">(자취 기본양념)</span>}
                  </span>
                  <span className="ing-amount">{ing.amount}</span>
                </li>
              ))}
            </ul>
          </div>

          <div className="steps-section">
            <h3>🍳 조리 순서</h3>
            <div className="step-timeline">
              {defaultSteps.map((step, idx) => (
                <div key={idx} className="timeline-item">
                  <div className="timeline-badge">{step.stepOrder || idx + 1}</div>
                  <div className="timeline-content">
                    <p className="instruction">{step.instruction}</p>
                    {step.tip && <p className="tip-note">💡 {step.tip}</p>}
                    {step.timerSeconds > 0 && (
                      <span className="timer-badge">⏱️ 권장 타이머 {step.timerSeconds}초</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
