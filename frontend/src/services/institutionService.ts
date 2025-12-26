import api from './api';

export interface Institution {
  id: number;
  name: string;
  description?: string;
  active: boolean;
  totalDonations?: number;
  createdAt: string;
}

export const institutionService = {
  getAll: async (): Promise<Institution[]> => {
    const response = await api.get<Institution[]>('/institutions');
    return response.data;
  },

  getById: async (id: number): Promise<Institution> => {
    const response = await api.get<Institution>(`/institutions/${id}`);
    return response.data;
  },

  create: async (data: Omit<Institution, 'id' | 'createdAt'>): Promise<Institution> => {
    const response = await api.post<Institution>('/institutions', data);
    return response.data;
  },

  update: async (id: number, data: Partial<Institution>): Promise<Institution> => {
    const response = await api.put<Institution>(`/institutions/${id}`, data);
    return response.data;
  },
};

