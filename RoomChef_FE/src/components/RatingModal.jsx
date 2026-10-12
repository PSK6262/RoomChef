import React, { useState } from 'react'
import { api } from '../utils/api'

export default function RatingModal({ recipe, onClose, onSuccess }) {
  const [rating, setRating] = useState(4.0)
  const [feedbackNote, setFeedbackNote] = useState('')
  const [loading, setLoading] = useState(false)
  const [resultMsg, setResultMsg] = useState('')

  const getRatingGuide = (val) => {
    if (val >= 4.0) return '⭐ 4~5점: 호평! AI 알고리즘에서 이 레시피 계열에 가산점이 붙어 더 자주 추천됩니다.'
    if (val <= 2.0) return '⚠️ 1~2점: 혹평! AI 알고리즘에서 취향을 반영해 추천 노출 빈도가 감점됩니다.'
    return '👌 3점: 무난함! 기존 노출 가중치를 그대로 유지합니다.'
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const msg = await api.submitEvaluation({
        recipeId: recipe.recipeId,
        rating,
        feedbackNote,
      })
      setResultMsg(msg || '평가가 성공적으로 등록되었습니다.')
      setTimeout(() => {
        onSuccess?.()
        onClose()
      }, 1200)
    } catch (err) {
      alert(err.message || '평가 등록에 실패했습니다. 로그인이 필요할 수 있습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>요리 실전 평가하기</h3>
          <button className="btn-close" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="modal-body">
          <p className="eval-recipe-title">
            <strong>{recipe.title}</strong>
          </p>
          <p className="eval-desc">직접 만들어본 맛은 어떠셨나요? 평점에 따라 내일 AI 추천 가중치가 바뀝니다.</p>

          <div className="star-selector">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                key={star}
                type="button"
                className={`star-btn ${rating >= star ? 'active' : ''}`}
                onClick={() => setRating(star)}
              >
                ★
              </button>
            ))}
            <span className="rating-value">{rating}점</span>
          </div>

          <div className={`rating-guide-box ${rating >= 4 ? 'bonus' : rating <= 2 ? 'penalty' : 'neutral'}`}>
            {getRatingGuide(rating)}
          </div>

          <textarea
            className="eval-textarea"
            placeholder="맛, 난이도, 꿀팁 등 메모를 남겨보세요. (선택)"
            value={feedbackNote}
            onChange={(e) => setFeedbackNote(e.target.value)}
            rows={3}
          />

          {resultMsg && <div className="result-notice">{resultMsg}</div>}
        </div>

        <div className="modal-footer">
          <button className="btn-secondary" onClick={onClose} disabled={loading}>
            취소
          </button>
          <button className="btn-primary" onClick={handleSubmit} disabled={loading}>
            {loading ? '반영 중...' : '평가 등록'}
          </button>
        </div>
      </div>
    </div>
  )
}
