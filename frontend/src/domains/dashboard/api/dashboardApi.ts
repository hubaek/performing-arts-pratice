import { apiClient } from '../../../shared/api/client';
import {
  DashboardOverview,
  PracticeStatistics,
  MemberStatistics,
  TeamStatistics,
  TrendStatistics
} from '../types';

const DASHBOARD_BASE_URL = '/dashboard';

/**
 * 대시보드 전체 현황 조회
 */
export const getDashboardOverview = async (): Promise<DashboardOverview> => {
  const response = await apiClient.get<DashboardOverview>(`${DASHBOARD_BASE_URL}/overview`);
  return response.data;
};

/**
 * 연습 관련 통계 조회
 */
export const getPracticeStatistics = async (): Promise<PracticeStatistics> => {
  const response = await apiClient.get<PracticeStatistics>(`${DASHBOARD_BASE_URL}/practices`);
  return response.data;
};

/**
 * 회원 관련 통계 조회 (관리자 전용)
 */
export const getMemberStatistics = async (): Promise<MemberStatistics> => {
  const response = await apiClient.get<MemberStatistics>(`${DASHBOARD_BASE_URL}/members`);
  return response.data;
};

/**
 * 팀 관련 통계 조회
 */
export const getTeamStatistics = async (): Promise<TeamStatistics> => {
  const response = await apiClient.get<TeamStatistics>(`${DASHBOARD_BASE_URL}/teams`);
  return response.data;
};

/**
 * 트렌드 분석 통계 조회 (관리자 전용)
 */
export const getTrendStatistics = async (): Promise<TrendStatistics> => {
  const response = await apiClient.get<TrendStatistics>(`${DASHBOARD_BASE_URL}/trends`);
  return response.data;
};

// 모든 API를 한 번에 내보내기
export const dashboardApi = {
  getDashboardOverview,
  getPracticeStatistics,
  getMemberStatistics,
  getTeamStatistics,
  getTrendStatistics,
};

export default dashboardApi;