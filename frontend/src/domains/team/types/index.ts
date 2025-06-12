// Team 도메인 타입 정의

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

export interface TeamUpdateRequest {
  name: string;
  description?: string;
  leader?: string;
}