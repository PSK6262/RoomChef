/**
 * [RoomChef 프론트엔드 통합 API 클라이언트]
 */

const BASE_URL = ''

export async function apiRequest(endpoint, options = {}) {
  const token = localStorage.getItem('roomchef_token')
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  }

  const response = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers,
  })

  if (response.status === 401) {
    localStorage.removeItem('roomchef_token')
    localStorage.removeItem('roomchef_user')
  }

  const result = await response.json().catch(() => null)
  if (!response.ok) {
    throw new Error(result?.message || '요청 처리 중 오류가 발생했습니다.')
  }

  return result?.data
}

export const api = {
  // 인증
  login: (data) => apiRequest('/api/auth/login', { method: 'POST', body: JSON.stringify(data) }),
  signup: (data) => apiRequest('/api/auth/signup', { method: 'POST', body: JSON.stringify(data) }),

  // 레시피
  getRecipes: (params = {}) => {
    const query = new URLSearchParams(params).toString()
    return apiRequest(`/api/recipes?${query}`)
  },
  getRecipeDetail: (recipeId) => apiRequest(`/api/recipes/${recipeId}`),
  matchRecipes: (data) => apiRequest('/api/recipes/match', { method: 'POST', body: JSON.stringify(data) }),

  // 냉장고
  getFridgeIngredients: () => apiRequest('/api/fridge'),
  addFridgeIngredient: (data) => apiRequest('/api/fridge', { method: 'POST', body: JSON.stringify(data) }),
  removeFridgeIngredient: (fridgeId) => apiRequest(`/api/fridge/${fridgeId}`, { method: 'DELETE' }),
  matchFridge: (allowPartial = true) => apiRequest(`/api/fridge/match?allowPartial=${allowPartial}`),

  // 평가 (1~5점 피드백 루프)
  submitEvaluation: (data) => apiRequest('/api/evaluations', { method: 'POST', body: JSON.stringify(data) }),
  getRecipeEvaluations: (recipeId) => apiRequest(`/api/evaluations/recipe/${recipeId}`),

  // AI 챗봇
  askChatbot: (data) => apiRequest('/api/chatbot/ask', { method: 'POST', body: JSON.stringify(data) }),
}
