import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Typography,
  Box,
  Chip,
  IconButton,
  CircularProgress,
  Alert
} from '@mui/material';
import {
  Add as AddIcon,
  Visibility as ViewIcon,
  Edit as EditIcon,
  Delete as DeleteIcon
} from '@mui/icons-material';
import { format } from 'date-fns';
import { practiceApi, PracticeListItem } from '../../api';

const TIME_FORMAT_LENGTH = 5; // HH:mm 형식

const PracticeList: React.FC = () => {
  const navigate = useNavigate();
  const [practices, setPractices] = useState<PracticeListItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadPractices();
  }, []);

  const loadPractices = async () => {
    try {
      setLoading(true);
      const data = await practiceApi.getAll();
      setPractices(data);
    } catch (err) {
      setError('연습 목록을 불러오는데 실패했습니다.');
      console.error('Error loading practices:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('정말로 삭제하시겠습니까?')) {
      try {
        await practiceApi.delete(id);
        await loadPractices(); // 목록 새로고침
      } catch (err) {
        setError('삭제에 실패했습니다.');
        console.error('Error deleting practice:', err);
      }
    }
  };

  const formatDate = (dateString: string) => {
    return format(new Date(dateString), 'yyyy.MM.dd');
  };

  const formatTime = (timeString: string) => {
    return timeString.substring(0, TIME_FORMAT_LENGTH); // HH:mm 형식으로 자르기
  };

  const getStatusColor = (isCompleted: boolean) => {
    return isCompleted ? 'success' : 'warning';
  };

  const getStatusText = (isCompleted: boolean) => {
    return isCompleted ? '완료' : '진행중';
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="300px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4" component="h1">
          연습일지 목록
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => navigate('/practices/new')}
        >
          새 연습 등록
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>제목</TableCell>
              <TableCell>날짜</TableCell>
              <TableCell>시간</TableCell>
              <TableCell>장소</TableCell>
              <TableCell>참석률</TableCell>
              <TableCell>상태</TableCell>
              <TableCell align="center">작업</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {practices.length === 0 ? (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  등록된 연습이 없습니다.
                </TableCell>
              </TableRow>
            ) : (
              practices.map((practice) => (
                <TableRow key={practice.id} hover>
                  <TableCell>
                    <Typography variant="subtitle2">
                      {practice.title}
                    </Typography>
                  </TableCell>
                  <TableCell>{formatDate(practice.practiceDate)}</TableCell>
                  <TableCell>
                    {formatTime(practice.startTime)} - {formatTime(practice.endTime)}
                  </TableCell>
                  <TableCell>{practice.location}</TableCell>
                  <TableCell>
                    {practice.totalParticipants > 0 ? (
                      <Typography variant="body2">
                        {practice.attendanceRate.toFixed(1)}%
                        <br />
                        <Typography variant="caption" color="text.secondary">
                          ({practice.totalParticipants}명 중 참석)
                        </Typography>
                      </Typography>
                    ) : (
                      <Typography variant="body2" color="text.secondary">
                        참석 기록 없음
                      </Typography>
                    )}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={getStatusText(practice.isCompleted)}
                      color={getStatusColor(practice.isCompleted)}
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      onClick={() => navigate(`/practices/${practice.id}`)}
                      title="상세보기"
                    >
                      <ViewIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => navigate(`/practices/${practice.id}/edit`)}
                      title="수정"
                      disabled={practice.isCompleted}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDelete(practice.id)}
                      title="삭제"
                      disabled={practice.isCompleted}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default PracticeList;