import React from 'react';
import {
  Card,
  CardContent,
  CardHeader,
  Box,
  Typography,
  LinearProgress,
} from '@mui/material';
import { MonthlyPracticeCount } from '../types';

interface MonthlyPracticeChartProps {
  data: MonthlyPracticeCount[];
  title?: string;
}

const MonthlyPracticeChart: React.FC<MonthlyPracticeChartProps> = ({ 
  data, 
  title = "월별 연습 현황" 
}) => {
  const maxCount = Math.max(...data.map(item => item.practiceCount), 1);

  return (
    <Card sx={{ height: '100%' }}>
      <CardHeader title={title} />
      <CardContent>
        <Box>
          {data.map((item, index) => (
            <Box key={index} sx={{ mb: 2 }}>
              <Box display="flex" justifyContent="space-between" alignItems="center" mb={1}>
                <Typography variant="body2" color="textSecondary">
                  {item.month}
                </Typography>
                <Typography variant="body2" fontWeight="bold">
                  {item.practiceCount}회
                </Typography>
              </Box>
              <LinearProgress
                variant="determinate"
                value={(item.practiceCount / maxCount) * 100}
                sx={{
                  height: 8,
                  borderRadius: 5,
                  backgroundColor: 'rgba(0, 0, 0, 0.1)',
                  '& .MuiLinearProgress-bar': {
                    borderRadius: 5,
                    backgroundColor: (theme) => theme.palette.primary.main,
                  },
                }}
              />
            </Box>
          ))}
        </Box>
        {data.length === 0 && (
          <Typography variant="body2" color="textSecondary" textAlign="center" py={4}>
            데이터가 없습니다.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default MonthlyPracticeChart;