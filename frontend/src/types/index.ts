// API 응답 타입 정의

export interface Practice {
  id: number;
  title: string;
  content: string;
  location: string;
  practiceDate: string; // ISO date string
  startTime: string; // HH:mm format
  endTime: string; // HH:mm format
  comment?: string;
  userId: number;
  teamId?: number;
  isCompleted: boolean;
  totalParticipants: number;
  presentCount: number;
  lateCount: number;
  absentCount: number;
  attendanceRate: number;
  practiceDurationInMinutes: number;
  createdAt: string;
  updatedAt: string;
}

export interface PracticeListItem {
  id: number;
  title: string;
  location: string;
  practiceDate: string;
  startTime: string;
  endTime: string;
  teamId?: number;
  isCompleted: boolean;
  totalParticipants: number;
  attendanceRate: number;
}

export interface PracticeCreateRequest {
  title: string;
  content: string;
  location: string;
  practiceDate: string;
  startTime: string;
  endTime: string;
  comment?: string;
  teamId?: number;
}

export interface PracticeUpdateRequest {
  title: string;
  content: string;
  location: string;
  practiceDate: string;
  startTime: string;
  endTime: string;
  comment?: string;
}

export interface Team {
  id: number;
  name: string;
  description?: string;
  status: string;
  leader?: string;
  memberCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface TeamListItem {
  id: number;
  name: string;
  status: string;
  leader?: string;
  memberCount: number;
}

export interface TeamCreateRequest {
  name: string;
  description?: string;
  leader?: string;
}

export interface PracticeParticipation {
  id: number;
  practiceId: number;
  userId: number;
  status: PracticeParticipationStatus;
  reason?: string;
  comment?: string;
  isExcused: boolean;
  isPresent: boolean;
  isAbsent: boolean;
  createdAt: string;
  updatedAt: string;
}

export enum PracticeParticipationStatus {
  ATTENDANCE = 'ATTENDANCE',
  LATE = 'LATE',
  ABSENT = 'ABSENT'
}

export interface PracticeParticipationCreateRequest {
  practiceId: number;
  userId: number;
  status: PracticeParticipationStatus;
  reason?: string;
  comment?: string;
  isExcused?: boolean;
}

export interface Member {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  birthDate: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  isActive: boolean;
  teamId?: number;
  createdAt: string;
  modifiedAt: string;
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