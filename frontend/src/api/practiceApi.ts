import { apiClient } from './client';
import { 
  Practice, 
  PracticeListItem, 
  PracticeCreateRequest, 
  PracticeUpdateRequest,
  PageResponse 
} from '../types';

export const practiceApi = {
  // 연습 목록 조회
  getAll: async (): Promise<PracticeListItem[]> => {
    const response = await apiClient.get<PracticeListItem[]>('/practices');
    return response.data;
  },

  // 페이지네이션된 연습 목록 조회
  getAllWithPaging: async (page = 0, size = 10): Promise<PageResponse<PracticeListItem>> => {
    const response = await apiClient.get<PageResponse<PracticeListItem>>(
      `/practices/paged?page=${page}&size=${size}`
    );
    return response.data;
  },

  // 연습 상세 조회
  getById: async (id: number): Promise<Practice> => {
    const response = await apiClient.get<Practice>(`/practices/${id}`);
    return response.data;
  },

  // 팀별 연습 목록 조회
  getByTeam: async (teamId: number): Promise<PracticeListItem[]> => {
    const response = await apiClient.get<PracticeListItem[]>(`/practices/team/${teamId}`);
    return response.data;
  },

  // 기간별 연습 목록 조회
  getByDateRange: async (startDate: string, endDate: string): Promise<PracticeListItem[]> => {
    const response = await apiClient.get<PracticeListItem[]>(
      `/practices/date-range?startDate=${startDate}&endDate=${endDate}`
    );
    return response.data;
  },

  // 내가 작성한 연습 목록 조회
  getMy: async (): Promise<PracticeListItem[]> => {
    const response = await apiClient.get<PracticeListItem[]>('/practices/my');
    return response.data;
  },

  // 연습 생성
  create: async (practice: PracticeCreateRequest): Promise<Practice> => {
    const response = await apiClient.post<Practice>('/practices', practice);
    return response.data;
  },

  // 연습 수정
  update: async (id: number, practice: PracticeUpdateRequest): Promise<Practice> => {
    const response = await apiClient.put<Practice>(`/practices/${id}`, practice);
    return response.data;
  },

  // 연습 삭제
  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/practices/${id}`);
  },

  // 연습 완료 처리
  complete: async (id: number): Promise<Practice> => {
    const response = await apiClient.patch<Practice>(`/practices/${id}/complete`);
    return response.data;
  }
};