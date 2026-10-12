import React from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { logout } from '../store/authSlice'

export default function Navbar({ activeTab, setActiveTab }) {
  const { user, isAuthenticated } = useSelector((state) => state.auth)
  const dispatch = useDispatch()

  return (
    <nav className="navbar">
      <div className="nav-container">
        <div className="nav-brand" onClick={() => setActiveTab('recipes')}>
          <span className="brand-icon">🍳</span>
          <span className="brand-title">방구석 쉐프</span>
        </div>

        <div className="nav-links">
          <button
            className={`nav-btn ${activeTab === 'recipes' ? 'active' : ''}`}
            onClick={() => setActiveTab('recipes')}
          >
            레시피 탐색
          </button>
          <button
            className={`nav-btn ${activeTab === 'fridge' ? 'active' : ''}`}
            onClick={() => setActiveTab('fridge')}
          >
            냉장고 파먹기
          </button>
        </div>

        <div className="nav-auth">
          {isAuthenticated ? (
            <div className="user-profile">
              <span className="user-nickname">{user?.nickname || user?.loginId} 셰프님</span>
              <button className="auth-btn logout" onClick={() => dispatch(logout())}>
                로그아웃
              </button>
            </div>
          ) : (
            <button className="auth-btn login" onClick={() => setActiveTab('login')}>
              로그인 / 회원가입
            </button>
          )}
        </div>
      </div>
    </nav>
  )
}
