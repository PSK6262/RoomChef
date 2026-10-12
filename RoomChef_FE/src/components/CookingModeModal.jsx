import React, { useState, useEffect } from 'react'

export default function CookingModeModal({ recipe, onClose }) {
  const steps = recipe.steps || [
    { stepOrder: 1, instruction: '재료를 깨끗이 씻고 손질합니다.', timerSeconds: 0, tip: '물기를 잘 털어주세요.' },
    { stepOrder: 2, instruction: '팬에 기름을 두르고 재료를 볶습니다.', timerSeconds: 180, tip: '중불 유지' },
    { stepOrder: 3, instruction: '양념을 넣고 골고루 섞어 마무리합니다.', timerSeconds: 60, tip: '불 끄고 참기름 한 방울' },
  ]

  const [currentStep, setCurrentStep] = useState(0)
  const [timeLeft, setTimeLeft] = useState(0)
  const [timerRunning, setTimerRunning] = useState(false)

  const activeStep = steps[currentStep] || steps[0]

  useEffect(() => {
    if (activeStep?.timerSeconds > 0) {
      setTimeLeft(activeStep.timerSeconds)
      setTimerRunning(false)
    } else {
      setTimeLeft(0)
      setTimerRunning(false)
    }
  }, [currentStep, activeStep])

  useEffect(() => {
    let interval = null
    if (timerRunning && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => prev - 1)
      }, 1000)
    } else if (timeLeft === 0 && timerRunning) {
      setTimerRunning(false)
      alert('⏰ 조리 타이머 시간이 종료되었습니다!')
    }
    return () => clearInterval(interval)
  }, [timerRunning, timeLeft])

  const formatTimer = (sec) => {
    const m = Math.floor(sec / 60)
    const s = sec % 60
    return `${m}:${s < 10 ? '0' : ''}${s}`
  }

  return (
    <div className="cooking-backdrop">
      <div className="cooking-container">
        <div className="cooking-header">
          <div className="cooking-title">
            <span className="badge-mode">👨‍🍳 조리 모드</span>
            <h2>{recipe.title}</h2>
          </div>
          <button className="btn-close-cooking" onClick={onClose}>
            ✕ 종료
          </button>
        </div>

        <div className="cooking-step-progress">
          {steps.map((_, idx) => (
            <div
              key={idx}
              className={`progress-dot ${idx === currentStep ? 'current' : idx < currentStep ? 'done' : ''}`}
              onClick={() => setCurrentStep(idx)}
            >
              {idx + 1}
            </div>
          ))}
        </div>

        <div className="cooking-card">
          <div className="step-number">STEP {activeStep.stepOrder || currentStep + 1}</div>
          <p className="step-instruction">{activeStep.instruction}</p>

          {activeStep.tip && (
            <div className="step-tip">
              💡 <strong>자취 팁:</strong> {activeStep.tip}
            </div>
          )}

          {activeStep.timerSeconds > 0 && (
            <div className="step-timer-box">
              <div className="timer-display">{formatTimer(timeLeft)}</div>
              <button
                className={`btn-timer ${timerRunning ? 'pause' : 'start'}`}
                onClick={() => setTimerRunning(!timerRunning)}
              >
                {timerRunning ? '⏸️ 일시정지' : '▶️ 타이머 시작'}
              </button>
            </div>
          )}
        </div>

        <div className="cooking-footer-controls">
          <button
            className="btn-step prev"
            disabled={currentStep === 0}
            onClick={() => setCurrentStep((prev) => Math.max(0, prev - 1))}
          >
            ◀ 이전 단계
          </button>
          <button
            className="btn-step next"
            onClick={() => {
              if (currentStep < steps.length - 1) {
                setCurrentStep((prev) => prev + 1)
              } else {
                alert('🎉 요리가 완성되었습니다! 맛있게 드세요.')
                onClose()
              }
            }}
          >
            {currentStep === steps.length - 1 ? '🎉 조리 완료' : '다음 단계 ▶'}
          </button>
        </div>
      </div>
    </div>
  )
}
