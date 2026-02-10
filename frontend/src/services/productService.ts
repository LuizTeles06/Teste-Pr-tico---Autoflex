import api from './api';
import { Product, ProductRawMaterial } from '../types';

const ENDPOINT = '/products';

export const productService = {
  getAll: async (): Promise<Product[]> => {
    const response = await api.get<Product[]>(ENDPOINT);
    return response.data;
  },

  getById: async (id: number): Promise<Product> => {
    const response = await api.get<Product>(`${ENDPOINT}/${id}`);
    return response.data;
  },

  create: async (product: Product): Promise<Product> => {
    const response = await api.post<Product>(ENDPOINT, product);
    return response.data;
  },

  update: async (id: number, product: Product): Promise<Product> => {
    const response = await api.put<Product>(`${ENDPOINT}/${id}`, product);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`${ENDPOINT}/${id}`);
  },

  addRawMaterial: async (productId: number, rawMaterial: ProductRawMaterial): Promise<Product> => {
    const response = await api.post<Product>(
      `${ENDPOINT}/${productId}/raw-materials`,
      rawMaterial
    );
    return response.data;
  },

  updateRawMaterial: async (
    productId: number,
    rawMaterialId: number,
    data: ProductRawMaterial
  ): Promise<Product> => {
    const response = await api.put<Product>(
      `${ENDPOINT}/${productId}/raw-materials/${rawMaterialId}`,
      data
    );
    return response.data;
  },

  removeRawMaterial: async (productId: number, rawMaterialId: number): Promise<Product> => {
    const response = await api.delete<Product>(
      `${ENDPOINT}/${productId}/raw-materials/${rawMaterialId}`
    );
    return response.data;
  },
};
