// Practice 도메인 타입 정의

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

// Practice participation 관련 타입들
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