import api from './api';
import { RawMaterial } from '../types';

const ENDPOINT = '/raw-materials';

export const rawMaterialService = {
  getAll: async (): Promise<RawMaterial[]> => {
    const response = await api.get<RawMaterial[]>(ENDPOINT);
    return response.data;
  },

  getById: async (id: number): Promise<RawMaterial> => {
    const response = await api.get<RawMaterial>(`${ENDPOINT}/${id}`);
    return response.data;
  },

  search: async (name: string): Promise<RawMaterial[]> => {
    const response = await api.get<RawMaterial[]>(`${ENDPOINT}/search`, {
      params: { name },
    });
    return response.data;
  },

  create: async (rawMaterial: RawMaterial): Promise<RawMaterial> => {
    const response = await api.post<RawMaterial>(ENDPOINT, rawMaterial);
    return response.data;
  },

  update: async (id: number, rawMaterial: RawMaterial): Promise<RawMaterial> => {
    const response = await api.put<RawMaterial>(`${ENDPOINT}/${id}`, rawMaterial);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`${ENDPOINT}/${id}`);
  },
};
