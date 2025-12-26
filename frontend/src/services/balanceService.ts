import api from './api';

export interface Balance {
  institutionId: number;
  institutionName: string;
  totalDonations: number;
  totalExpenses: number;
  totalOutgoingTransfers: number;
  totalIncomingTransfers: number;
  availableBalance: number;
}

export const balanceService = {
  getHostCompanyBalance: async (): Promise<Balance> => {
    const response = await api.get<Balance>('/balance/host-company');
    return response.data;
  },

  getBalance: async (institutionId: number): Promise<Balance> => {
    const response = await api.get<Balance>(`/balance/institution/${institutionId}`);
    return response.data;
  },

  getAllBalances: async (): Promise<Balance[]> => {
    const response = await api.get<Balance[]>('/balance/all');
    return response.data;
  },
};

