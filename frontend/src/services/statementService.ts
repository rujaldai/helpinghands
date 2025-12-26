import api from './api';

export interface Statement {
  id: number;
  donationId: number;
  userId: number;
  userName: string;
  message?: string;
  donationAmount: number;
  createdAt: string;
}

export interface StatementRequest {
  donationId: number;
  message?: string;
}

export const statementService = {
  create: async (data: StatementRequest): Promise<Statement> => {
    const response = await api.post<Statement>('/statements', data);
    return response.data;
  },

  getAll: async (): Promise<Statement[]> => {
    const response = await api.get<Statement[]>('/statements');
    return response.data;
  },

  getByDonation: async (donationId: number): Promise<Statement[]> => {
    const response = await api.get<Statement[]>(`/statements/donation/${donationId}`);
    return response.data;
  },
};

