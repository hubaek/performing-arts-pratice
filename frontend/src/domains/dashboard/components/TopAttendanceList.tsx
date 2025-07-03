import React from 'react';
import {
  Card,
  CardContent,
  CardHeader,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Typography,
  Box,
  Chip,
} from '@mui/material';
import { EmojiEvents as TrophyIcon } from '@mui/icons-material';
import { TopAttendanceMember } from '../types';

interface TopAttendanceListProps {
  data: TopAttendanceMember[];
  title?: string;
}

const TopAttendanceList: React.FC<TopAttendanceListProps> = ({ 
  data, 
  title = "출석률 상위 회원" 
}) => {
  const getAvatarColor = (index: number) => {
    if (index === 0) return 'gold';
    if (index === 1) return 'silver';
    if (index === 2) return '#cd7f32'; // bronze
    return 'grey';
  };

  const getRankChipColor = (index: number): "default" | "primary" | "secondary" | "error" | "info" | "success" | "warning" => {
    if (index === 0) return 'warning'; // gold
    if (index === 1) return 'info'; // silver
    if (index === 2) return 'success'; // bronze
    return 'default';
  };

  return (
    <Card sx={{ height: '100%' }}>
      <CardHeader title={title} />
      <CardContent sx={{ pt: 0 }}>
        <List disablePadding>
          {data.map((member, index) => (
            <ListItem 
              key={member.memberId} 
              sx={{
                px: 0,
                py: 1,
                borderBottom: index < data.length - 1 ? 1 : 0,
                borderColor: 'divider',
              }}
              aria-label={`${index + 1}위: ${member.memberName}, 출석률 ${member.attendanceRate.toFixed(1)}%`}
            >
              <ListItemAvatar>
                <Avatar
                  sx={{
                    bgcolor: getAvatarColor(index),
                    color: 'white',
                    width: 32,
                    height: 32,
                  }}
                >
                  {index < 3 ? <TrophyIcon fontSize="small" /> : index + 1}
                </Avatar>
              </ListItemAvatar>
              
              <ListItemText
                primary={
                  <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography variant="subtitle2" fontWeight="bold">
                      {member.memberName}
                    </Typography>
                    <Chip
                      label={`${index + 1}위`}
                      size="small"
                      color={getRankChipColor(index)}
                    />
                  </Box>
                }
                secondary={
                  <Box>
                    <Typography variant="body2" color="textSecondary">
                      {member.department}
                    </Typography>
                    <Box display="flex" justifyContent="space-between" mt={0.5}>
                      <Typography variant="body2" color="primary.main" fontWeight="bold">
                        출석률: {member.attendanceRate.toFixed(1)}%
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        총 연습: {member.totalPractices}회
                      </Typography>
                    </Box>
                  </Box>
                }
              />
            </ListItem>
          ))}
        </List>
        
        {data.length === 0 && (
          <Typography variant="body2" color="textSecondary" textAlign="center" py={4}>
            데이터가 없습니다.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default TopAttendanceList;