import api from './api';
import { ProductionSuggestion } from '../types';

const ENDPOINT = '/production';

export const productionService = {
  getSuggestion: async (): Promise<ProductionSuggestion> => {
    const response = await api.get<ProductionSuggestion>(`${ENDPOINT}/suggestion`);
    return response.data;
  },
};
