import { apiClient } from '../../../shared/api/client';
import { AuthResponse, LoginRequest, SignupRequest, User, TokenRefreshRequest } from '../types';

export const authApi = {
  // 로그인
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  // 회원가입
  signup: async (userData: SignupRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/signup', userData);
    return response.data;
  },

  // 토큰 갱신
  refreshToken: async (request: TokenRefreshRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/refresh', request);
    return response.data;
  },

  // 현재 사용자 정보 조회
  getCurrentUser: async (): Promise<User> => {
    const response = await apiClient.get<User>('/auth/me');
    return response.data;
  },

  // 로그아웃
  logout: async (): Promise<void> => {
    await apiClient.post('/auth/logout');
  },

  // 이메일 중복 확인
  checkEmailExists: async (email: string): Promise<boolean> => {
    const response = await apiClient.get<boolean>(`/members/check-email?email=${encodeURIComponent(email)}`);
    return response.data;
  },

  // 고유코드 중복 확인
  checkUniqueCodeExists: async (uniqueCode: string): Promise<boolean> => {
    const response = await apiClient.get<boolean>(`/members/check-unique-code?uniqueCode=${encodeURIComponent(uniqueCode)}`);
    return response.data;
  }
};