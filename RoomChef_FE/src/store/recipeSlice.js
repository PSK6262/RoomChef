import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  recipes: [],
  selectedRecipe: null,
  filters: {
    keyword: '',
    difficulty: '',
    onePanYn: '',
    sourceType: '',
  },
  loading: false,
}

const recipeSlice = createSlice({
  name: 'recipe',
  initialState,
  reducers: {
    setRecipes(state, action) {
      state.recipes = action.payload
    },
    setSelectedRecipe(state, action) {
      state.selectedRecipe = action.payload
    },
    setFilters(state, action) {
      state.filters = { ...state.filters, ...action.payload }
    },
    setLoading(state, action) {
      state.loading = action.payload
    },
  },
})

export const { setRecipes, setSelectedRecipe, setFilters, setLoading } = recipeSlice.actions
export default recipeSlice.reducer
