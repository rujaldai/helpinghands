import api from './api';

export interface Donation {
  id: number;
  userId: number;
  userName: string;
  institutionId?: number;
  institutionName?: string;
  causeId?: number;
  causeName?: string;
  amount: number;
  currency: string;
  createdAt: string;
}

export interface DonationRequest {
  amount: number;
  currency?: string;
  institutionId?: number;
  causeId?: number;
  toHostCompany?: boolean;
  guestId?: string;
}

export const donationService = {
  createDonation: async (data: DonationRequest): Promise<Donation> => {
    const response = await api.post<Donation>('/donations', data);
    return response.data;
  },

  getMyDonations: async (): Promise<Donation[]> => {
    const response = await api.get<Donation[]>('/donations/my');
    return response.data;
  },

  getAllDonations: async (): Promise<Donation[]> => {
    const response = await api.get<Donation[]>('/donations');
    return response.data;
  },
};

