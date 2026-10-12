import React, { useState } from 'react'
import { useDispatch } from 'react-redux'
import { setCredentials } from '../store/authSlice'
import { api } from '../utils/api'

export default function Login({ onSuccess }) {
  const [isSignup, setIsSignup] = useState(false)
  const [loginId, setLoginId] = useState('')
  const [password, setPassword] = useState('')
  const [nickname, setNickname] = useState('')
  const [email, setEmail] = useState('')
  const [errorMsg, setErrorMsg] = useState('')
  const [loading, setLoading] = useState(false)

  const dispatch = useDispatch()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setErrorMsg('')
    setLoading(true)

    try {
      if (isSignup) {
        await api.signup({ loginId, password, nickname, email })
        alert('회원가입이 완료되었습니다! 로그인해주세요.')
        setIsSignup(false)
      } else {
        const data = await api.login({ loginId, password })
        if (data) {
          dispatch(
            setCredentials({
              user: {
                userId: data.userId,
                loginId: data.loginId,
                nickname: data.nickname,
                email: data.email,
              },
              token: data.token,
            })
          )
          onSuccess?.()
        }
      }
    } catch (err) {
      setErrorMsg(err.message || '인증 중 오류가 발생했습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page-container auth-page">
      <div className="auth-box">
        <h2>{isSignup ? '🍳 방구석 셰프 회원가입' : '🍳 방구석 셰프 로그인'}</h2>
        <p className="auth-subtitle">
          {isSignup ? '내 냉장고와 레시피 평가를 저장해보세요.' : '환영합니다! 오늘의 맛있는 한 끼를 시작하세요.'}
        </p>

        {errorMsg && <div className="error-alert">{errorMsg}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>아이디</label>
            <input
              type="text"
              required
              placeholder="아이디를 입력하세요"
              value={loginId}
              onChange={(e) => setLoginId(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>비밀번호</label>
            <input
              type="password"
              required
              placeholder="비밀번호를 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          {isSignup && (
            <>
              <div className="form-group">
                <label>닉네임</label>
                <input
                  type="text"
                  required
                  placeholder="사용할 닉네임"
                  value={nickname}
                  onChange={(e) => setNickname(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label>이메일</label>
                <input
                  type="email"
                  required
                  placeholder="email@example.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
            </>
          )}

          <button type="submit" className="btn-auth-submit" disabled={loading}>
            {loading ? '처리 중...' : isSignup ? '가입하기' : '로그인'}
          </button>
        </form>

        <div className="auth-switch">
          {isSignup ? (
            <p>
              이미 계정이 있으신가요?{' '}
              <button type="button" onClick={() => setIsSignup(false)}>
                로그인
              </button>
            </p>
          ) : (
            <p>
              아직 회원이 아니신가요?{' '}
              <button type="button" onClick={() => setIsSignup(true)}>
                회원가입
              </button>
            </p>
          )}
        </div>
      </div>
    </div>
  )
}
