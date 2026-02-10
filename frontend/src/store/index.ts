import { configureStore } from '@reduxjs/toolkit';
import productReducer from './slices/productSlice';
import rawMaterialReducer from './slices/rawMaterialSlice';
import productionReducer from './slices/productionSlice';

export const store = configureStore({
  reducer: {
    products: productReducer,
    rawMaterials: rawMaterialReducer,
    production: productionReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
