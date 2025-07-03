// Auth 도메인 타입 정의
import { Role, LegacyTimestampedEntity } from '../../../shared/types';

export interface User extends LegacyTimestampedEntity {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  birthDate?: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  uniqueCode?: string;
  status: 'ACTIVE' | 'LEAVE_OF_ABSENCE' | 'INACTIVE';
  role: Role;
  teamId?: number;
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
  phoneNumber: string;
  birthDate: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  uniqueCode: string;
  teamId?: number;
}

export interface TokenRefreshRequest {
  refreshToken: string;
}