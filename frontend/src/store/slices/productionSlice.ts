import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { ProductionSuggestion } from '../../types';
import { productionService } from '../../services/productionService';

interface ProductionState {
  suggestion: ProductionSuggestion | null;
  loading: boolean;
  error: string | null;
}

const initialState: ProductionState = {
  suggestion: null,
  loading: false,
  error: null,
};

// Async thunks
export const fetchProductionSuggestion = createAsyncThunk(
  'production/fetchSuggestion',
  async (_, { rejectWithValue }) => {
    try {
      return await productionService.getSuggestion();
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to fetch production suggestion');
    }
  }
);

const productionSlice = createSlice({
  name: 'production',
  initialState,
  reducers: {
    clearSuggestion: (state) => {
      state.suggestion = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProductionSuggestion.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProductionSuggestion.fulfilled, (state, action: PayloadAction<ProductionSuggestion>) => {
        state.loading = false;
        state.suggestion = action.payload;
      })
      .addCase(fetchProductionSuggestion.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearSuggestion, clearError } = productionSlice.actions;
export default productionSlice.reducer;
