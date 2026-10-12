import React, { useState } from 'react'
import Navbar from './components/Navbar'
import RecipeList from './pages/RecipeList'
import RecipeDetail from './pages/RecipeDetail'
import Fridge from './pages/Fridge'
import Login from './pages/Login'
import ChatbotModal from './components/ChatbotModal'
import RatingModal from './components/RatingModal'
import CookingModeModal from './components/CookingModeModal'
import './App.css'

export default function App() {
  const [activeTab, setActiveTab] = useState('recipes')
  const [selectedRecipe, setSelectedRecipe] = useState(null)
  const [ratingRecipe, setRatingRecipe] = useState(null)
  const [cookingRecipe, setCookingRecipe] = useState(null)
  const [showChatModal, setShowChatModal] = useState(false)

  return (
    <div className="app-root">
      <Navbar
        activeTab={activeTab}
        setActiveTab={(tab) => {
          setSelectedRecipe(null)
          setActiveTab(tab)
        }}
      />

      <main className="main-content">
        {selectedRecipe ? (
          <RecipeDetail
            recipe={selectedRecipe}
            onBack={() => setSelectedRecipe(null)}
            onRate={(r) => setRatingRecipe(r)}
            onStartCooking={(r) => setCookingRecipe(r)}
          />
        ) : activeTab === 'recipes' ? (
          <RecipeList
            onSelectRecipe={(r) => setSelectedRecipe(r)}
            onRateRecipe={(r) => setRatingRecipe(r)}
            onStartCooking={(r) => setCookingRecipe(r)}
          />
        ) : activeTab === 'fridge' ? (
          <Fridge
            onSelectRecipe={(r) => setSelectedRecipe(r)}
            onRateRecipe={(r) => setRatingRecipe(r)}
            onStartCooking={(r) => setCookingRecipe(r)}
          />
        ) : activeTab === 'login' ? (
          <Login onSuccess={() => setActiveTab('recipes')} />
        ) : null}
      </main>

      {/* 우측 하단 둥근 플로팅 챗봇 버튼 */}
      <button
        className="floating-chatbot-btn"
        onClick={() => setShowChatModal(true)}
        title="AI 챗봇 열기"
      >
        챗봇
      </button>

      {/* 모달 컴포넌트들 */}
      {showChatModal && (
        <ChatbotModal
          onClose={() => setShowChatModal(false)}
          onSelectRecipe={(r) => setSelectedRecipe(r)}
        />
      )}

      {ratingRecipe && (
        <RatingModal
          recipe={ratingRecipe}
          onClose={() => setRatingRecipe(null)}
          onSuccess={() => setRatingRecipe(null)}
        />
      )}

      {cookingRecipe && (
        <CookingModeModal
          recipe={cookingRecipe}
          onClose={() => setCookingRecipe(null)}
        />
      )}
    </div>
  )
}
