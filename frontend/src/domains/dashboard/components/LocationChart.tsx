import React from 'react';
import {
  Card,
  CardContent,
  CardHeader,
  Box,
  Typography,
  Chip,
} from '@mui/material';
import { LocationPracticeCount } from '../types';

interface LocationChartProps {
  data: LocationPracticeCount[];
  title?: string;
}

const LocationChart: React.FC<LocationChartProps> = ({ 
  data, 
  title = "장소별 연습 분포" 
}) => {
  const totalPractices = data.reduce((sum, item) => sum + item.practiceCount, 0);

  return (
    <Card sx={{ height: '100%' }} aria-label={`${title} 차트`}>
      <CardHeader title={title} />
      <CardContent>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {data.map((item, index) => {
            const percentage = totalPractices > 0 ? (item.practiceCount / totalPractices) * 100 : 0;
            
            return (
              <Box key={index}>
                <Box
                  sx={{
                    p: 2,
                    border: 1,
                    borderColor: 'grey.300',
                    borderRadius: 2,
                    backgroundColor: 'grey.50',
                    '&:hover': {
                      backgroundColor: 'grey.100',
                    },
                  }}
                >
                  <Box display="flex" justifyContent="space-between" alignItems="center" mb={1}>
                    <Typography variant="subtitle2" fontWeight="bold">
                      {item.location}
                    </Typography>
                    <Chip 
                      label={`${item.practiceCount}회`} 
                      size="small" 
                      color="primary" 
                    />
                  </Box>
                  
                  <Box display="flex" justifyContent="space-between" alignItems="center" mb={1}>
                    <Typography variant="body2" color="textSecondary">
                      전체 대비: {percentage.toFixed(1)}%
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      출석률: {item.averageAttendanceRate.toFixed(1)}%
                    </Typography>
                  </Box>
                  
                  {/* 간단한 프로그레스 바 */}
                  <Box
                    sx={{
                      width: '100%',
                      height: 6,
                      backgroundColor: 'grey.300',
                      borderRadius: 3,
                      overflow: 'hidden',
                    }}
                    role="progressbar"
                    aria-valuenow={percentage}
                    aria-valuemin={0}
                    aria-valuemax={100}
                    aria-label={`${item.location} 연습 비율: ${percentage.toFixed(1)}%`}
                  >
                    <Box
                      sx={{
                        width: `${percentage}%`,
                        height: '100%',
                        backgroundColor: 'primary.main',
                        transition: 'width 0.3s ease',
                      }}
                    />
                  </Box>
                </Box>
              </Box>
            );
          })}
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

export default LocationChart;