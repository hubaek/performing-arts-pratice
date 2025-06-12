// Auth 도메인 타입 정의
import { Role } from '../../../shared/types';

export interface User {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  birthDate?: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  uniqueCode?: string;
  status: 'ACTIVE' | 'LEAVE_OF_ABSENCE' | 'INACTIVE';
  role: Role;
  teamId?: number;
  createdAt: string;
  modifiedAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  memberId: number;
  name: string;
  email: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber?: string;
  birthDate: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  uniqueCode?: string;
  teamId?: number;
}

export interface TokenRefreshRequest {
  refreshToken: string;
}