import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { RawMaterial } from '../../types';
import { rawMaterialService } from '../../services/rawMaterialService';

interface RawMaterialState {
  items: RawMaterial[];
  selectedRawMaterial: RawMaterial | null;
  loading: boolean;
  error: string | null;
}

const initialState: RawMaterialState = {
  items: [],
  selectedRawMaterial: null,
  loading: false,
  error: null,
};

// Async thunks
export const fetchRawMaterials = createAsyncThunk(
  'rawMaterials/fetchAll',
  async (_, { rejectWithValue }) => {
    try {
      return await rawMaterialService.getAll();
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to fetch raw materials');
    }
  }
);

export const fetchRawMaterialById = createAsyncThunk(
  'rawMaterials/fetchById',
  async (id: number, { rejectWithValue }) => {
    try {
      return await rawMaterialService.getById(id);
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to fetch raw material');
    }
  }
);

export const createRawMaterial = createAsyncThunk(
  'rawMaterials/create',
  async (rawMaterial: RawMaterial, { rejectWithValue }) => {
    try {
      return await rawMaterialService.create(rawMaterial);
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to create raw material');
    }
  }
);

export const updateRawMaterial = createAsyncThunk(
  'rawMaterials/update',
  async ({ id, rawMaterial }: { id: number; rawMaterial: RawMaterial }, { rejectWithValue }) => {
    try {
      return await rawMaterialService.update(id, rawMaterial);
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to update raw material');
    }
  }
);

export const deleteRawMaterial = createAsyncThunk(
  'rawMaterials/delete',
  async (id: number, { rejectWithValue }) => {
    try {
      await rawMaterialService.delete(id);
      return id;
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } };
      return rejectWithValue(err.response?.data?.message || 'Failed to delete raw material');
    }
  }
);

const rawMaterialSlice = createSlice({
  name: 'rawMaterials',
  initialState,
  reducers: {
    clearSelectedRawMaterial: (state) => {
      state.selectedRawMaterial = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch all
      .addCase(fetchRawMaterials.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRawMaterials.fulfilled, (state, action: PayloadAction<RawMaterial[]>) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchRawMaterials.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Fetch by ID
      .addCase(fetchRawMaterialById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchRawMaterialById.fulfilled, (state, action: PayloadAction<RawMaterial>) => {
        state.loading = false;
        state.selectedRawMaterial = action.payload;
      })
      .addCase(fetchRawMaterialById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Create
      .addCase(createRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createRawMaterial.fulfilled, (state, action: PayloadAction<RawMaterial>) => {
        state.loading = false;
        state.items.push(action.payload);
      })
      .addCase(createRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Update
      .addCase(updateRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateRawMaterial.fulfilled, (state, action: PayloadAction<RawMaterial>) => {
        state.loading = false;
        const index = state.items.findIndex((rm) => rm.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
        state.selectedRawMaterial = action.payload;
      })
      .addCase(updateRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Delete
      .addCase(deleteRawMaterial.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteRawMaterial.fulfilled, (state, action: PayloadAction<number>) => {
        state.loading = false;
        state.items = state.items.filter((rm) => rm.id !== action.payload);
      })
      .addCase(deleteRawMaterial.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearSelectedRawMaterial, clearError } = rawMaterialSlice.actions;
export default rawMaterialSlice.reducer;
