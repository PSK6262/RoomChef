import { configureStore } from '@reduxjs/toolkit'
import authReducer from './authSlice'
import recipeReducer from './recipeSlice'
import fridgeReducer from './fridgeSlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    recipe: recipeReducer,
    fridge: fridgeReducer,
  },
})
