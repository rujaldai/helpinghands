import api from './api';

export interface Cause {
  id: number;
  name: string;
  description?: string;
  active: boolean;
  totalDonations?: number;
  createdAt: string;
}

export const causeService = {
  getAll: async (): Promise<Cause[]> => {
    const response = await api.get<Cause[]>('/causes');
    return response.data;
  },

  getById: async (id: number): Promise<Cause> => {
    const response = await api.get<Cause>(`/causes/${id}`);
    return response.data;
  },

  create: async (data: Omit<Cause, 'id' | 'createdAt'>): Promise<Cause> => {
    const response = await api.post<Cause>('/causes', data);
    return response.data;
  },

  update: async (id: number, data: Partial<Cause>): Promise<Cause> => {
    const response = await api.put<Cause>(`/causes/${id}`, data);
    return response.data;
  },
};

