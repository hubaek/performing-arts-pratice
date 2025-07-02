import React, { useState, useEffect, useCallback } from 'react';
import {
  Container,
  Typography,
  Box,
  CircularProgress,
  Alert,
  Tabs,
  Tab,
  Paper,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  FitnessCenter as PracticeIcon,
  People as PeopleIcon,
  Group as GroupIcon,
  TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';

import { dashboardApi } from '../api/dashboardApi';
import {
  DashboardOverview,
  PracticeStatistics,
  MemberStatistics,
  TeamStatistics,
  TrendStatistics,
} from '../types';

import OverviewCards from './OverviewCards';
import MonthlyPracticeChart from './MonthlyPracticeChart';
import LocationChart from './LocationChart';
import TopAttendanceList from './TopAttendanceList';
import TeamStatsTable from './TeamStatsTable';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

const TabPanel: React.FC<TabPanelProps> = ({ children, value, index }) => {
  return (
    <div role="tabpanel" hidden={value !== index}>
      {value === index && <Box sx={{ py: 3 }}>{children}</Box>}
    </div>
  );
};

const Dashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 데이터 상태
  const [overviewData, setOverviewData] = useState<DashboardOverview | null>(null);
  const [practiceData, setPracticeData] = useState<PracticeStatistics | null>(null);
  const [memberData, setMemberData] = useState<MemberStatistics | null>(null);
  const [teamData, setTeamData] = useState<TeamStatistics | null>(null);
  const [trendData, setTrendData] = useState<TrendStatistics | null>(null);

  // 데이터 로딩 함수들
  const loadOverviewData = useCallback(async () => {
    try {
      const data = await dashboardApi.getDashboardOverview();
      setOverviewData(data);
    } catch (err) {
      console.error('전체 현황 데이터 로딩 실패:', err);
    }
  }, []);

  const loadPracticeData = useCallback(async () => {
    try {
      const data = await dashboardApi.getPracticeStatistics();
      setPracticeData(data);
    } catch (err) {
      console.error('연습 통계 데이터 로딩 실패:', err);
    }
  }, []);

  const loadMemberData = useCallback(async () => {
    try {
      const data = await dashboardApi.getMemberStatistics();
      setMemberData(data);
    } catch (err) {
      console.error('회원 통계 데이터 로딩 실패:', err);
      // 권한 없음 에러일 수 있으므로 조용히 실패 처리
    }
  }, []);

  const loadTeamData = useCallback(async () => {
    try {
      const data = await dashboardApi.getTeamStatistics();
      setTeamData(data);
    } catch (err) {
      console.error('팀 통계 데이터 로딩 실패:', err);
    }
  }, []);

  const loadTrendData = useCallback(async () => {
    try {
      const data = await dashboardApi.getTrendStatistics();
      setTrendData(data);
    } catch (err) {
      console.error('트렌드 데이터 로딩 실패:', err);
      // 권한 없음 에러일 수 있으므로 조용히 실패 처리
    }
  }, []);

  // 초기 데이터 로딩
  useEffect(() => {
    const loadInitialData = async () => {
      setLoading(true);
      setError(null);

      try {
        await Promise.all([
          loadOverviewData(),
          loadPracticeData(),
          loadTeamData(),
        ]);

        // 관리자 전용 데이터는 별도로 로딩 (실패해도 전체 로딩을 막지 않음)
        await Promise.allSettled([
          loadMemberData(),
          loadTrendData(),
        ]);

      } catch (err) {
        setError('데이터를 불러오는 중 오류가 발생했습니다.');
        console.error('대시보드 데이터 로딩 실패:', err);
      } finally {
        setLoading(false);
      }
    };

    loadInitialData();
  }, [loadOverviewData, loadPracticeData, loadMemberData, loadTeamData, loadTrendData]);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  if (loading) {
    return (
      <Container maxWidth="lg">
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <CircularProgress size={60} />
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg">
        <Box py={4}>
          <Alert severity="error">{error}</Alert>
        </Box>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      <Box py={4}>
        {/* 헤더 */}
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          대시보드
        </Typography>
        <Typography variant="body1" color="textSecondary" mb={4}>
          공연예술단 활동 현황을 한눈에 확인하세요
        </Typography>

        {/* 탭 네비게이션 */}
        <Paper sx={{ mb: 3 }}>
          <Tabs 
            value={activeTab} 
            onChange={handleTabChange}
            variant="scrollable"
            scrollButtons="auto"
          >
            <Tab 
              icon={<DashboardIcon />} 
              label="전체 현황" 
              iconPosition="start"
            />
            <Tab 
              icon={<PracticeIcon />} 
              label="연습 통계" 
              iconPosition="start"
            />
            {memberData && (
              <Tab 
                icon={<PeopleIcon />} 
                label="회원 통계" 
                iconPosition="start"
              />
            )}
            <Tab 
              icon={<GroupIcon />} 
              label="팀 통계" 
              iconPosition="start"
            />
            {trendData && (
              <Tab 
                icon={<TrendingUpIcon />} 
                label="트렌드 분석" 
                iconPosition="start"
              />
            )}
          </Tabs>
        </Paper>

        {/* 전체 현황 탭 */}
        <TabPanel value={activeTab} index={0}>
          {overviewData && <OverviewCards data={overviewData} />}
        </TabPanel>

        {/* 연습 통계 탭 */}
        <TabPanel value={activeTab} index={1}>
          {practiceData && (
            <Box 
              sx={{
                display: 'grid',
                gridTemplateColumns: {
                  xs: '1fr',
                  md: 'repeat(2, 1fr)'
                },
                gap: 3
              }}
            >
              <MonthlyPracticeChart data={practiceData.monthlyPracticeCounts} />
              <LocationChart data={practiceData.practicesByLocation} />
            </Box>
          )}
        </TabPanel>

        {/* 회원 통계 탭 */}
        {memberData && (
          <TabPanel value={activeTab} index={2}>
            <Box 
              sx={{
                display: 'grid',
                gridTemplateColumns: {
                  xs: '1fr',
                  md: 'repeat(2, 1fr)'
                },
                gap: 3
              }}
            >
              <TopAttendanceList data={memberData.topAttendanceMembers} />
              <MonthlyPracticeChart 
                data={memberData.monthlyNewMembers.map(item => ({
                  month: item.month,
                  practiceCount: item.newMemberCount,
                }))}
                title="월별 신규 가입자"
              />
            </Box>
          </TabPanel>
        )}

        {/* 팀 통계 탭 */}
        <TabPanel value={activeTab} index={memberData ? 3 : 2}>
          {teamData && (
            <Box>
              <TeamStatsTable data={teamData.teamStats} />
            </Box>
          )}
        </TabPanel>

        {/* 트렌드 분석 탭 */}
        {trendData && (
          <TabPanel value={activeTab} index={memberData ? 4 : 3}>
            <Box 
              sx={{
                display: 'grid',
                gridTemplateColumns: {
                  xs: '1fr',
                  md: 'repeat(2, 1fr)'
                },
                gap: 3
              }}
            >
              <MonthlyPracticeChart 
                data={trendData.monthlyTrends.map(item => ({
                  month: item.month,
                  practiceCount: item.practiceCount,
                }))}
                title="월별 연습 트렌드"
              />
              <MonthlyPracticeChart 
                data={trendData.monthlyTrends.map(item => ({
                  month: item.month,
                  practiceCount: item.newMemberCount,
                }))}
                title="월별 신규 가입자 트렌드"
              />
            </Box>
          </TabPanel>
        )}
      </Box>
    </Container>
  );
};

export default Dashboard;