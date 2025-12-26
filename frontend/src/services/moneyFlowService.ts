import api from './api';

export interface MoneyFlow {
  id: number;
  donationId: number;
  userId: number;
  userName: string;
  donationAmount: number;
  flowAmount: number;
  currency: string;
  expenseId?: number;
  expenseDescription?: string;
  transferId?: number;
  transferDescription?: string;
  createdAt: string;
}

export const moneyFlowService = {
  getAll: async (): Promise<MoneyFlow[]> => {
    const response = await api.get<MoneyFlow[]>('/money-flows');
    return response.data;
  },

  getByUser: async (userId: number): Promise<MoneyFlow[]> => {
    const response = await api.get<MoneyFlow[]>(`/money-flows/user/${userId}`);
    return response.data;
  },

  getByDonation: async (donationId: number): Promise<MoneyFlow[]> => {
    const response = await api.get<MoneyFlow[]>(`/money-flows/donation/${donationId}`);
    return response.data;
  },
};

