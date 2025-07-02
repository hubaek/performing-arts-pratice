import React from 'react';
import { Box } from '@mui/material';
import { 
  People as PeopleIcon,
  Group as GroupIcon,
  FitnessCenter as PracticeIcon,
  TrendingUp as TrendingUpIcon,
  PersonAdd as PersonAddIcon
} from '@mui/icons-material';
import StatCard from './StatCard';
import { DashboardOverview } from '../types';

interface OverviewCardsProps {
  data: DashboardOverview;
}

const OverviewCards: React.FC<OverviewCardsProps> = ({ data }) => {
  return (
    <Box>
      <Box 
        sx={{
          display: 'grid',
          gridTemplateColumns: {
            xs: '1fr',
            sm: 'repeat(2, 1fr)',
            md: 'repeat(3, 1fr)',
            lg: 'repeat(4, 1fr)'
          },
          gap: 3
        }}
      >
        {/* 전체 회원 수 */}
        <StatCard
          title="전체 회원"
          value={data.totalMembers}
          subtitle={`활성: ${data.activeMembers}명`}
          icon={<PeopleIcon />}
          color="primary"
        />

        {/* 전체 팀 수 */}
        <StatCard
          title="전체 팀"
          value={data.totalTeams}
          subtitle={`활성: ${data.activeTeams}팀`}
          icon={<GroupIcon />}
          color="secondary"
        />

        {/* 전체 연습 수 */}
        <StatCard
          title="전체 연습"
          value={data.totalPractices}
          subtitle={`이번 달: ${data.thisMonthPractices}회`}
          icon={<PracticeIcon />}
          color="success"
        />

        {/* 전체 출석률 */}
        <StatCard
          title="전체 출석률"
          value={`${data.overallAttendanceRate.toFixed(1)}%`}
          subtitle={`완료된 연습: ${data.completedPractices}회`}
          icon={<TrendingUpIcon />}
          color="info"
        />

        {/* 이번 달 신규 가입자 */}
        <StatCard
          title="이번 달 신규 가입"
          value={data.newMembersThisMonth}
          subtitle="명"
          icon={<PersonAddIcon />}
          color="warning"
        />

        {/* 휴학 회원 수 */}
        <StatCard
          title="휴학 회원"
          value={data.leaveOfAbsenceMembers}
          subtitle="명"
          icon={<PeopleIcon />}
          color="warning"
        />

        {/* 비활성 회원 수 */}
        <StatCard
          title="비활성 회원"
          value={data.inactiveMembers}
          subtitle="명"
          icon={<PeopleIcon />}
          color="error"
        />

        {/* 연습 완료률 */}
        <StatCard
          title="연습 완료률"
          value={`${((data.completedPractices / data.totalPractices) * 100).toFixed(1)}%`}
          subtitle={`${data.completedPractices}/${data.totalPractices}`}
          icon={<TrendingUpIcon />}
          color="success"
        />
      </Box>
    </Box>
  );
};

export default OverviewCards;