import api from './api';

export interface User {
  id: number;
  email: string;
  name: string;
  role: 'GUEST' | 'DONATOR' | 'ADMIN';
  guestId?: string;
  active: boolean;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  guestId?: string;
}

export const authService = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/register', data);
    return response.data;
  },

  createGuest: async (): Promise<User> => {
    const response = await api.post<User>('/users/guest');
    return response.data;
  },

  getGuest: async (guestId: string): Promise<User> => {
    const response = await api.get<User>(`/users/guest/${guestId}`);
    return response.data;
  },
};

