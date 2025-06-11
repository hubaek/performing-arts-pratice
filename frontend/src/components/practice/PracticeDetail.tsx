import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Paper,
  Typography,
  Box,
  Grid,
  Button,
  Chip,
  Divider,
  Alert,
  CircularProgress,
  Card,
  CardContent
} from '@mui/material';
import {
  Edit as EditIcon,
  ArrowBack as ArrowBackIcon,
  CheckCircle as CheckCircleIcon
} from '@mui/icons-material';
import { format } from 'date-fns';
import { practiceApi, Practice } from '../../api';

const PracticeDetail: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  
  const [practice, setPractice] = useState<Practice | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      loadPractice();
    }
  }, [id]);

  const loadPractice = async () => {
    if (!id) return;
    
    try {
      setLoading(true);
      const data = await practiceApi.getById(Number(id));
      setPractice(data);
    } catch (err) {
      setError('연습 정보를 불러오는데 실패했습니다.');
      console.error('Error loading practice:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleComplete = async () => {
    if (!practice || !id) return;

    if (window.confirm('연습을 완료 처리하시겠습니까?')) {
      try {
        const updatedPractice = await practiceApi.complete(Number(id));
        setPractice(updatedPractice);
      } catch (err) {
        setError('완료 처리에 실패했습니다.');
        console.error('Error completing practice:', err);
      }
    }
  };

  const formatDate = (dateString: string) => {
    return format(new Date(dateString), 'yyyy년 MM월 dd일');
  };

  const formatTime = (timeString: string) => {
    return timeString.substring(0, 5);
  };

  const formatDateTime = (dateTimeString: string) => {
    return format(new Date(dateTimeString), 'yyyy.MM.dd HH:mm');
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="300px">
        <CircularProgress />
      </Box>
    );
  }

  if (error || !practice) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error || '연습 정보를 찾을 수 없습니다.'}
      </Alert>
    );
  }

  return (
    <Box>
      <Box display="flex" alignItems="center" mb={3}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/practices')}
          sx={{ mr: 2 }}
        >
          목록으로
        </Button>
        <Typography variant="h4" component="h1" sx={{ flexGrow: 1 }}>
          연습일지 상세
        </Typography>
        {!practice.isCompleted && (
          <>
            <Button
              variant="outlined"
              startIcon={<EditIcon />}
              onClick={() => navigate(`/practices/${id}/edit`)}
              sx={{ mr: 1 }}
            >
              수정
            </Button>
            <Button
              variant="contained"
              startIcon={<CheckCircleIcon />}
              onClick={handleComplete}
              color="success"
            >
              완료 처리
            </Button>
          </>
        )}
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3 }}>
            <Box display="flex" alignItems="center" mb={2}>
              <Typography variant="h5" sx={{ flexGrow: 1 }}>
                {practice.title}
              </Typography>
              <Chip
                label={practice.isCompleted ? '완료' : '진행중'}
                color={practice.isCompleted ? 'success' : 'warning'}
              />
            </Box>

            <Grid container spacing={2} mb={3}>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle2" color="text.secondary">
                  연습 날짜
                </Typography>
                <Typography variant="body1">
                  {formatDate(practice.practiceDate)}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle2" color="text.secondary">
                  연습 시간
                </Typography>
                <Typography variant="body1">
                  {formatTime(practice.startTime)} - {formatTime(practice.endTime)}
                  <Typography variant="caption" color="text.secondary" sx={{ ml: 1 }}>
                    ({practice.practiceDurationInMinutes}분)
                  </Typography>
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle2" color="text.secondary">
                  연습 장소
                </Typography>
                <Typography variant="body1">
                  {practice.location}
                </Typography>
              </Grid>
            </Grid>

            <Divider sx={{ my: 2 }} />

            <Typography variant="subtitle2" color="text.secondary" mb={1}>
              연습 내용
            </Typography>
            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap', mb: 3 }}>
              {practice.content}
            </Typography>

            {practice.comment && (
              <>
                <Typography variant="subtitle2" color="text.secondary" mb={1}>
                  특이사항 및 코멘트
                </Typography>
                <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
                  {practice.comment}
                </Typography>
              </>
            )}
          </Paper>
        </Grid>

        <Grid item xs={12} md={4}>
          <Card sx={{ mb: 2 }}>
            <CardContent>
              <Typography variant="h6" mb={2}>
                참석 통계
              </Typography>
              
              {practice.totalParticipants > 0 ? (
                <Grid container spacing={2}>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2" color="text.secondary">
                      총 인원
                    </Typography>
                    <Typography variant="h6">
                      {practice.totalParticipants}명
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="subtitle2" color="text.secondary">
                      출석률
                    </Typography>
                    <Typography variant="h6" color="primary">
                      {practice.attendanceRate.toFixed(1)}%
                    </Typography>
                  </Grid>
                  <Grid item xs={4}>
                    <Typography variant="body2" color="text.secondary">
                      출석
                    </Typography>
                    <Typography variant="body1" color="success.main">
                      {practice.presentCount}명
                    </Typography>
                  </Grid>
                  <Grid item xs={4}>
                    <Typography variant="body2" color="text.secondary">
                      지각
                    </Typography>
                    <Typography variant="body1" color="warning.main">
                      {practice.lateCount}명
                    </Typography>
                  </Grid>
                  <Grid item xs={4}>
                    <Typography variant="body2" color="text.secondary">
                      결석
                    </Typography>
                    <Typography variant="body1" color="error.main">
                      {practice.absentCount}명
                    </Typography>
                  </Grid>
                </Grid>
              ) : (
                <Typography variant="body2" color="text.secondary">
                  아직 참석 기록이 없습니다.
                </Typography>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Typography variant="h6" mb={2}>
                등록 정보
              </Typography>
              <Typography variant="body2" color="text.secondary" mb={1}>
                등록일시
              </Typography>
              <Typography variant="body2" mb={2}>
                {formatDateTime(practice.createdAt)}
              </Typography>
              
              <Typography variant="body2" color="text.secondary" mb={1}>
                수정일시
              </Typography>
              <Typography variant="body2">
                {formatDateTime(practice.updatedAt)}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default PracticeDetail;