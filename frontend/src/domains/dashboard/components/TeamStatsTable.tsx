import React, { useCallback } from 'react';
import {
  Card,
  CardContent,
  CardHeader,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Chip,
  Box,
} from '@mui/material';
import { TeamDetailStat } from '../types';

interface TeamStatsTableProps {
  data: TeamDetailStat[];
  title?: string;
}

const TeamStatsTable: React.FC<TeamStatsTableProps> = ({ 
  data, 
  title = "팀별 상세 통계" 
}) => {
  const getAttendanceRateColor = useCallback((rate: number): "default" | "primary" | "secondary" | "error" | "info" | "success" | "warning" => {
    if (rate >= 90) return 'success';
    if (rate >= 80) return 'info';
    if (rate >= 70) return 'warning';
    return 'error';
  }, []);

  return (
    <Card sx={{ height: '100%' }}>
      <CardHeader title={title} />
      <CardContent sx={{ pt: 0 }}>
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>팀명</TableCell>
                <TableCell>팀장</TableCell>
                <TableCell align="center">전체 인원</TableCell>
                <TableCell align="center">활성 인원</TableCell>
                <TableCell align="center">총 연습</TableCell>
                <TableCell align="center">이번 달</TableCell>
                <TableCell align="center">출석률</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data.map((team) => (
                <TableRow 
                  key={team.teamId}
                  hover
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                  aria-label={`${team.teamName} 팀 통계: 전체 ${team.memberCount}명, 활성 ${team.activeMemberCount}명, 출석률 ${team.averageAttendanceRate.toFixed(1)}%`}
                >
                  <TableCell component="th" scope="row">
                    <Typography variant="subtitle2" fontWeight="bold">
                      {team.teamName}
                    </Typography>
                  </TableCell>
                  <TableCell>
                    <Typography variant="body2">
                      {team.leader}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={`${team.memberCount}명`} 
                      size="small" 
                      variant="outlined"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={`${team.activeMemberCount}명`} 
                      size="small" 
                      color="primary"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant="body2">
                      {team.totalPractices}회
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant="body2">
                      {team.thisMonthPractices}회
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={`${team.averageAttendanceRate.toFixed(1)}%`}
                      size="small"
                      color={getAttendanceRateColor(team.averageAttendanceRate)}
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        
        {data.length === 0 && (
          <Box py={4}>
            <Typography variant="body2" color="textSecondary" textAlign="center">
              데이터가 없습니다.
            </Typography>
          </Box>
        )}
      </CardContent>
    </Card>
  );
};

export default TeamStatsTable;