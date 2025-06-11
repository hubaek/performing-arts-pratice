import { apiClient } from './client';
import { Team, TeamListItem, TeamCreateRequest } from '../types';

export const teamApi = {
  // 모든 팀 조회
  getAll: async (): Promise<TeamListItem[]> => {
    const response = await apiClient.get<TeamListItem[]>('/teams');
    return response.data;
  },

  // 활성 팀만 조회
  getActive: async (): Promise<TeamListItem[]> => {
    const response = await apiClient.get<TeamListItem[]>('/teams/active');
    return response.data;
  },

  // 팀 상세 조회
  getById: async (id: number): Promise<Team> => {
    const response = await apiClient.get<Team>(`/teams/${id}`);
    return response.data;
  },

  // 상태별 팀 조회
  getByStatus: async (status: string): Promise<TeamListItem[]> => {
    const response = await apiClient.get<TeamListItem[]>(`/teams/status/${status}`);
    return response.data;
  },

  // 팀 생성
  create: async (team: TeamCreateRequest): Promise<Team> => {
    const response = await apiClient.post<Team>('/teams', team);
    return response.data;
  },

  // 팀 수정
  update: async (id: number, team: TeamCreateRequest): Promise<Team> => {
    const response = await apiClient.put<Team>(`/teams/${id}`, team);
    return response.data;
  },

  // 팀 삭제
  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/teams/${id}`);
  },

  // 팀 활성화
  activate: async (id: number): Promise<Team> => {
    const response = await apiClient.patch<Team>(`/teams/${id}/activate`);
    return response.data;
  },

  // 팀 비활성화
  deactivate: async (id: number): Promise<Team> => {
    const response = await apiClient.patch<Team>(`/teams/${id}/deactivate`);
    return response.data;
  },

  // 팀원 수 업데이트
  updateMemberCount: async (id: number, memberCount: number): Promise<Team> => {
    const response = await apiClient.patch<Team>(`/teams/${id}/member-count?memberCount=${memberCount}`);
    return response.data;
  }
};