// 공통 타입 정의

// 공통 열거형 타입
export enum Role {
  USER = 'USER',
  ADMIN = 'ADMIN'
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export enum MemberStatus {
  ACTIVE = 'ACTIVE',
  LEAVE_OF_ABSENCE = 'LEAVE_OF_ABSENCE',
  INACTIVE = 'INACTIVE'
}

// 공통 유틸리티 타입
export interface BaseEntity {
  id: number;
  createdAt: string;
  updatedAt: string;
}

// 타임스탬프 전용 인터페이스 (modifiedAt 사용)
export interface LegacyTimestampedEntity {
  createdAt: string;
  modifiedAt: string;
}

// 표준 타임스탬프 인터페이스 (updatedAt 사용)
export interface TimestampedEntity {
  createdAt: string;
  updatedAt: string;
}

// 멤버 관련 타입 (공통으로 사용됨)
export interface Member extends LegacyTimestampedEntity {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  birthDate: string;
  gender: Gender;
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  isActive: boolean;
  role: Role;
  teamId?: number;
}

export interface MemberListItem {
  id: number;
  name: string;
  email: string;
  department?: string;
  position?: string;
  teamId?: number;
  isActive: boolean;
}

// API 응답 래퍼 타입
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

// 페이지네이션 타입
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}