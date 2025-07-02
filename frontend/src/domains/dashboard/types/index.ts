// 대시보드 전체 현황 타입
export interface DashboardOverview {
  totalMembers: number;
  activeMembers: number;
  leaveOfAbsenceMembers: number;
  inactiveMembers: number;
  totalTeams: number;
  activeTeams: number;
  totalPractices: number;
  thisMonthPractices: number;
  completedPractices: number;
  overallAttendanceRate: number;
  newMembersThisMonth: number;
}

// 연습 통계 타입
export interface PracticeStatistics {
  totalPractices: number;
  completedPractices: number;
  completionRate: number;
  averageAttendanceRate: number;
  monthlyPracticeCounts: MonthlyPracticeCount[];
  practicesByLocation: LocationPracticeCount[];
}

export interface MonthlyPracticeCount {
  month: string; // YYYY-MM 형식
  practiceCount: number;
}

export interface LocationPracticeCount {
  location: string;
  practiceCount: number;
  averageAttendanceRate: number;
}

// 회원 통계 타입
export interface MemberStatistics {
  activeMemberCount: number;
  leaveOfAbsenceMemberCount: number;
  inactiveMemberCount: number;
  membersByDepartment: DepartmentMemberCount[];
  membersByJoinYear: JoinYearMemberCount[];
  monthlyNewMembers: MonthlyNewMember[];
  topAttendanceMembers: TopAttendanceMember[];
}

export interface DepartmentMemberCount {
  department: string;
  memberCount: number;
  averageAttendanceRate: number;
}

export interface JoinYearMemberCount {
  joinYear: number;
  memberCount: number;
}

export interface MonthlyNewMember {
  month: string; // YYYY-MM 형식
  newMemberCount: number;
}

export interface TopAttendanceMember {
  memberId: number;
  memberName: string;
  department: string;
  attendanceRate: number;
  totalPractices: number;
}

// 팀 통계 타입
export interface TeamStatistics {
  totalTeams: number;
  activeTeams: number;
  averageTeamSize: number;
  teamStats: TeamDetailStat[];
}

export interface TeamDetailStat {
  teamId: number;
  teamName: string;
  leader: string;
  memberCount: number;
  activeMemberCount: number;
  totalPractices: number;
  averageAttendanceRate: number;
  thisMonthPractices: number;
}

// 트렌드 분석 타입
export interface TrendStatistics {
  monthlyTrends: MonthlyTrend[];
  weeklyTrends: WeeklyTrend[];
}

export interface MonthlyTrend {
  month: string; // YYYY-MM 형식
  practiceCount: number;
  memberCount: number;
  averageAttendanceRate: number;
  newMemberCount: number;
  completedPracticeCount: number;
}

export interface WeeklyTrend {
  week: string; // YYYY-WW 형식
  practiceCount: number;
  averageAttendanceRate: number;
  totalParticipants: number;
}

// API 응답 타입
export interface DashboardApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
}