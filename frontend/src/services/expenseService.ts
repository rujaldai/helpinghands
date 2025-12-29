import api from './api';
import type {ExpenseCategory} from "./expenseCategory.ts";


export interface Expense {
  id: number;
  institutionId: number;
  institutionName: string;
  category: ExpenseCategory;
  amount: number;
  currency: string;
  description?: string;
  recipient?: string;
  recipientInstitutionId?: number;
  recipientInstitutionName?: string;
  createdAt: string;
}

export interface ExpenseRequest {
  amount: number;
  currency?: string;
  category: ExpenseCategory;
  description?: string;
  recipient?: string;
  recipientInstitutionId?: number;
}

export const expenseService = {
  getAll: async (): Promise<Expense[]> => {
    const response = await api.get<Expense[]>('/expenses');
    return response.data;
  },

  getByInstitution: async (institutionId: number): Promise<Expense[]> => {
    const response = await api.get<Expense[]>(`/expenses/institution/${institutionId}`);
    return response.data;
  },

  create: async (data: ExpenseRequest): Promise<Expense> => {
    const response = await api.post<Expense>('/expenses', data);
    return response.data;
  },
};

