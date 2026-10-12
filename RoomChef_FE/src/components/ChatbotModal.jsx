import React, { useState } from 'react'
import { api } from '../utils/api'

export default function ChatbotModal({ onClose, onSelectRecipe }) {
  const [messages, setMessages] = useState([
    {
      sender: 'ai',
      text: '안녕하세요! 방구석 셰프 AI입니다. 🍳\n오늘 냉장고에 남은 재료나 드시고 싶은 요리가 있나요? "스팸이랑 김치로 뭐 할 수 있어?", "월급 전날 3000원 컷" 처럼 편하게 물어보세요!',
    },
  ])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const [recommendations, setRecommendations] = useState([])
  const [substituteTips, setSubstituteTips] = useState([])

  const handleSend = async (textToSend) => {
    const query = textToSend || input
    if (!query.trim() || loading) return

    const newMsgs = [...messages, { sender: 'user', text: query }]
    setMessages(newMsgs)
    if (!textToSend) setInput('')
    setLoading(true)

    try {
      const response = await api.askChatbot({ message: query })
      if (response) {
        setMessages([...newMsgs, { sender: 'ai', text: response.reply }])
        setRecommendations(response.recommendations || [])
        setSubstituteTips(response.substituteTips || [])
      }
    } catch (err) {
      setMessages([
        ...newMsgs,
        { sender: 'ai', text: '죄송합니다. 챗봇 처리 중 오류가 발생했습니다: ' + err.message },
      ])
    } finally {
      setLoading(false)
    }
  }

  const quickPrompts = [
    '스팸이랑 양파로 만들 수 있는 요리',
    '설거지 적은 원팬 요리 추천해줘',
    '어제 술 마셨는데 간단한 해장 요리',
    '월급 전날 가성비 초간단 요리',
  ]

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="chatbot-modal" onClick={(e) => e.stopPropagation()}>
        <div className="chatbot-header">
          <div className="ai-title">
            <span className="ai-icon">🤖</span>
            <div>
              <h3>방구석 셰프 AI</h3>
              <p className="ai-sub">냉장고 파먹기 & 대체 재료 컨설턴트</p>
            </div>
          </div>
          <button className="btn-close" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="chatbot-body">
          <div className="chat-messages">
            {messages.map((m, idx) => (
              <div key={idx} className={`chat-bubble ${m.sender}`}>
                <div className="bubble-content">{m.text}</div>
              </div>
            ))}
            {loading && (
              <div className="chat-bubble ai loading">
                <div className="bubble-content">생각하는 중... 🍳</div>
              </div>
            )}
          </div>

          {substituteTips.length > 0 && (
            <div className="substitute-panel">
              <h4>💡 AI 추천 재료 대체 팁</h4>
              <ul>
                {substituteTips.map((tip, idx) => (
                  <li key={idx}>{tip}</li>
                ))}
              </ul>
            </div>
          )}

          {recommendations.length > 0 && (
            <div className="recommendations-panel">
              <h4>📋 추천 요리 결과 ({recommendations.length}건)</h4>
              <div className="recom-cards">
                {recommendations.slice(0, 3).map((r, idx) => (
                  <div
                    key={idx}
                    className="recom-item"
                    onClick={() => {
                      onSelectRecipe(r.recipe)
                      onClose()
                    }}
                  >
                    <div className="recom-header">
                      <span className="recom-title">{r.recipe.title}</span>
                      <span className={`recom-match-badge ${r.fullyMatch ? 'full' : 'partial'}`}>
                        {r.fullyMatch ? '완전 가능 (100%)' : `부족 재료 ${r.missingIngredientsCount}개`}
                      </span>
                    </div>
                    {!r.fullyMatch && r.missingIngredients?.length > 0 && (
                      <p className="recom-missing">
                        추가 필요: {r.missingIngredients.join(', ')}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            </div>
          )}

          <div className="quick-prompts">
            {quickPrompts.map((q, idx) => (
              <button key={idx} className="quick-chip" onClick={() => handleSend(q)}>
                {q}
              </button>
            ))}
          </div>
        </div>

        <form
          className="chatbot-input-bar"
          onSubmit={(e) => {
            e.preventDefault()
            handleSend()
          }}
        >
          <input
            type="text"
            placeholder="재료나 상황을 질문해보세요..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button type="submit" disabled={loading || !input.trim()}>
            전송
          </button>
        </form>
      </div>
    </div>
  )
}
