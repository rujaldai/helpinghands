import api from './api';
import type { Statement } from './statementService';

export interface DashboardStats {
  totalDonations: number;
  totalDonators: number;
  totalInstitutions: number;
  totalCauses: number;
  topDonators: TopDonator[];
  topInstitutions: TopInstitution[];
  topCauses: TopCause[];
  recentStatements: Statement[];
}

export interface TopDonator {
  userId: number;
  userName: string;
  totalAmount: number;
}

export interface TopInstitution {
  institutionId: number;
  institutionName: string;
  totalAmount: number;
}

export interface TopCause {
  causeId: number;
  causeName: string;
  totalAmount: number;
}

export const dashboardService = {
  getStats: async (): Promise<DashboardStats> => {
    const response = await api.get<DashboardStats>('/dashboard/stats');
    return response.data;
  },
};

