import React from 'react'
import { formatDifficulty, formatTime, formatRating } from '../utils/formatUtils'

export default function RecipeCard({ recipe, onSelect, onRate, onStartCooking }) {
  const diff = formatDifficulty(recipe.difficulty)
  const isAiBoosted = recipe.scoreWeight && recipe.scoreWeight >= 1.2

  return (
    <div className="recipe-card">
      <div className="card-thumb" onClick={() => onSelect(recipe)}>
        {recipe.thumbnailUrl ? (
          <img src={recipe.thumbnailUrl} alt={recipe.title} />
        ) : (
          <div className="thumb-placeholder">🍳 {recipe.channelName || '자취 레시피'}</div>
        )}
        <div className="thumb-tags">
          <span className="badge" style={{ backgroundColor: diff.color }}>
            {diff.text}
          </span>
          {recipe.onePanYn === 'Y' && <span className="badge onepan">원팬</span>}
          {recipe.microwaveYn === 'Y' && <span className="badge microwave">전자레인지</span>}
          {isAiBoosted && <span className="badge ai-boost">⭐ AI 추천 가산</span>}
        </div>
      </div>

      <div className="card-body">
        <div className="card-meta">
          <span className="channel-name">{recipe.channelName || '유튜브 자취요리'}</span>
          <span className="cook-time">⏱️ {formatTime(recipe.cookTimeMin)}</span>
        </div>

        <h3 className="card-title" onClick={() => onSelect(recipe)}>
          {recipe.title}
        </h3>

        <div className="card-footer">
          <div className="rating-info">
            <span className="star">★</span>
            <span className="score">{formatRating(recipe.avgRating)}</span>
            <span className="eval-count">({recipe.evalCount || 0}명 평가)</span>
          </div>

          <div className="card-actions">
            <button className="btn-rate" onClick={() => onRate(recipe)}>
              ⭐ 평가
            </button>
            <button className="btn-cook" onClick={() => onStartCooking(recipe)}>
              👨‍🍳 조리모드
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
