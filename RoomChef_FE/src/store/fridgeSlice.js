import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  ingredients: [],
  matchResults: [],
  loading: false,
}

const fridgeSlice = createSlice({
  name: 'fridge',
  initialState,
  reducers: {
    setIngredients(state, action) {
      state.ingredients = action.payload
    },
    addIngredient(state, action) {
      state.ingredients.unshift(action.payload)
    },
    removeIngredient(state, action) {
      state.ingredients = state.ingredients.filter((item) => item.fridgeId !== action.payload)
    },
    setMatchResults(state, action) {
      state.matchResults = action.payload
    },
    setLoading(state, action) {
      state.loading = action.payload
    },
  },
})

export const { setIngredients, addIngredient, removeIngredient, setMatchResults, setLoading } = fridgeSlice.actions
export default fridgeSlice.reducer
