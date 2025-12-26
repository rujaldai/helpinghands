import api from './api';

export interface Transfer {
  id: number;
  fromInstitutionId: number;
  fromInstitutionName: string;
  toInstitutionId: number;
  toInstitutionName: string;
  amount: number;
  currency: string;
  description?: string;
  createdAt: string;
}

export interface TransferRequest {
  toInstitutionId: number;
  amount: number;
  currency?: string;
  description?: string;
}

export const transferService = {
  getAll: async (): Promise<Transfer[]> => {
    const response = await api.get<Transfer[]>('/transfers');
    return response.data;
  },

  getByInstitution: async (institutionId: number): Promise<Transfer[]> => {
    const response = await api.get<Transfer[]>(`/transfers/institution/${institutionId}`);
    return response.data;
  },

  createFromHost: async (data: TransferRequest): Promise<Transfer> => {
    const response = await api.post<Transfer>('/transfers/from-host', data);
    return response.data;
  },

  createFromInstitution: async (institutionId: number, data: TransferRequest): Promise<Transfer> => {
    const response = await api.post<Transfer>(`/transfers/from-institution/${institutionId}`, data);
    return response.data;
  },
};

