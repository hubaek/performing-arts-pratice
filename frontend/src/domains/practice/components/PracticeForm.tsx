import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress
} from '@mui/material';
import { practiceApi } from '../api/practiceApi';
import { teamApi } from '../../team/api/teamApi';
import { PracticeCreateRequest } from '../types';
import { TeamListItem } from '../../team/types';

const LOADING_SPINNER_SIZE = 20;

// 연습 폼 검증 스키마
const validatePracticeForm = (formData: PracticeCreateRequest) => {
  const errors: {[key: string]: string} = {};

  // 필수 필드 검증
  if (!formData.title.trim()) errors.title = '연습 제목을 입력해주세요';
  if (!formData.content.trim()) errors.content = '연습 내용을 입력해주세요';
  if (!formData.location.trim()) errors.location = '연습 장소를 입력해주세요';
  if (!formData.practiceDate) errors.practiceDate = '연습 날짜를 선택해주세요';
  if (!formData.startTime) errors.startTime = '시작 시간을 입력해주세요';
  if (!formData.endTime) errors.endTime = '종료 시간을 입력해주세요';

  // 시간 유효성 검증
  if (formData.startTime && formData.endTime) {
    const startTime = new Date(`2000-01-01T${formData.startTime}`);
    const endTime = new Date(`2000-01-01T${formData.endTime}`);
    
    if (endTime <= startTime) {
      errors.endTime = '종료 시간은 시작 시간보다 늦어야 합니다';
    }
  }

  // 날짜 유효성 검증
  if (formData.practiceDate) {
    if (!/^\d{4}-\d{2}-\d{2}$/.test(formData.practiceDate)) {
      errors.practiceDate = '올바른 날짜 형식을 입력해주세요 (YYYY-MM-DD)';
    } else {
      // 선택한 날짜가 유효한 날짜인지 확인
      const selectedDate = new Date(formData.practiceDate);
      if (isNaN(selectedDate.getTime())) {
        errors.practiceDate = '올바른 날짜를 선택해주세요';
      }
    }
  }

  // 시간 형식 검증
  if (formData.startTime && !/^\d{2}:\d{2}$/.test(formData.startTime)) {
    errors.startTime = '올바른 시간 형식을 입력해주세요 (HH:mm)';
  }
  if (formData.endTime && !/^\d{2}:\d{2}$/.test(formData.endTime)) {
    errors.endTime = '올바른 시간 형식을 입력해주세요 (HH:mm)';
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors
  };
};

const PracticeForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEdit = !!id;

  const [formData, setFormData] = useState<PracticeCreateRequest>({
    title: '',
    content: '',
    location: '',
    practiceDate: '',
    startTime: '',
    endTime: '',
    comment: '',
    teamId: undefined
  });

  const [teams, setTeams] = useState<TeamListItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [formErrors, setFormErrors] = useState<{[key: string]: string}>({});

  const loadTeams = async () => {
    try {
      const data = await teamApi.getActive();
      setTeams(data);
    } catch (err) {
      console.error('Error loading teams:', err);
    }
  };

  const loadPractice = useCallback(async () => {
    if (!id) return;
    
    try {
      setLoading(true);
      const practice = await practiceApi.getById(Number(id));
      setFormData({
        title: practice.title,
        content: practice.content,
        location: practice.location,
        practiceDate: practice.practiceDate,
        startTime: practice.startTime,
        endTime: practice.endTime,
        comment: practice.comment || '',
        teamId: practice.teamId
      });
    } catch (err) {
      setError('연습 정보를 불러오는데 실패했습니다.');
      console.error('Error loading practice:', err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadTeams();
    if (isEdit) {
      loadPractice();
    }
  }, [id, isEdit, loadPractice]);

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'teamId' ? (value ? Number(value) : undefined) : value
    }));
    
    // 입력 시 해당 필드 에러 제거
    setFormErrors(prev => {
      if (prev[name]) {
        return {
          ...prev,
          [name]: ''
        };
      }
      return prev;
    });
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    // 폼 검증
    const { isValid, errors } = validatePracticeForm(formData);
    setFormErrors(errors);
    
    if (!isValid) {
      return;
    }
    
    try {
      setLoading(true);
      setError(null); // 기존 오류 메시지 초기화
      
      // 디버깅용 요청 데이터 로깅
      console.log('연습 등록/수정 요청 데이터:', {
        formData,
        isEdit,
        id,
        timestamp: new Date().toISOString()
      });
      
      if (isEdit) {
        await practiceApi.update(Number(id), formData);
        console.log('연습 수정 성공');
      } else {
        const result = await practiceApi.create(formData);
        console.log('연습 등록 성공:', result);
      }
      navigate('/practices');
    } catch (err: any) {
      console.error('연습 저장 오류 상세 정보:', {
        error: err,
        response: err.response,
        request: err.request,
        config: err.config,
        formData,
        timestamp: new Date().toISOString()
      });
      
      // API 응답에서 상세 오류 메시지 추출
      let errorMessage = isEdit ? '수정에 실패했습니다.' : '등록에 실패했습니다.';
      let debugInfo = '';
      
      if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.response?.data?.error) {
        errorMessage = err.response.data.error;
      } else if (err.response?.status) {
        switch (err.response.status) {
          case 400:
            errorMessage = '입력 데이터가 올바르지 않습니다.';
            debugInfo = ' 입력한 정보를 다시 확인해주세요.';
            break;
          case 401:
            errorMessage = '로그인이 필요합니다.';
            debugInfo = ' 다시 로그인해주세요.';
            break;
          case 403:
            errorMessage = '권한이 없습니다.';
            debugInfo = ' 관리자에게 문의하세요.';
            break;
          case 404:
            errorMessage = '요청한 리소스를 찾을 수 없습니다.';
            debugInfo = ' 페이지를 새로고침 후 다시 시도해주세요.';
            break;
          case 500:
            errorMessage = '서버 오류가 발생했습니다.';
            debugInfo = ' 잠시 후 다시 시도해주세요.';
            break;
          default:
            errorMessage = `오류가 발생했습니다. (상태 코드: ${err.response.status})`;
            debugInfo = ' 개발자 도구의 콘솔을 확인해주세요.';
        }
      } else if (err.code === 'NETWORK_ERROR' || err.message.includes('Network Error')) {
        errorMessage = '네트워크 연결을 확인해주세요.';
        debugInfo = ' 인터넷 연결 상태를 확인하고 다시 시도해주세요.';
      } else if (err.message) {
        errorMessage = err.message;
      }
      
      // 개발 환경에서는 디버그 정보도 포함
      if (process.env.NODE_ENV === 'development') {
        errorMessage += debugInfo;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEdit) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="300px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" component="h1" mb={3}>
        {isEdit ? '연습일지 수정' : '새 연습 등록'}
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Paper sx={{ p: 3 }}>
        <form onSubmit={handleSubmit}>
          <Box display="flex" flexDirection="column" gap={3}>
            <TextField
              fullWidth
              label="연습 제목"
              name="title"
              value={formData.title}
              onChange={handleChange}
              error={!!formErrors.title}
              helperText={formErrors.title}
              required
            />

            <Box display="flex" flexDirection={{ xs: 'column', md: 'row' }} gap={2}>
              <TextField
                fullWidth
                label="연습 날짜"
                name="practiceDate"
                type="date"
                value={formData.practiceDate}
                onChange={handleChange}
                error={!!formErrors.practiceDate}
                helperText={formErrors.practiceDate}
                InputLabelProps={{ shrink: true }}
                required
              />
              <TextField
                fullWidth
                label="시작 시간"
                name="startTime"
                type="time"
                value={formData.startTime}
                onChange={handleChange}
                error={!!formErrors.startTime}
                helperText={formErrors.startTime}
                InputLabelProps={{ shrink: true }}
                required
              />
              <TextField
                fullWidth
                label="종료 시간"
                name="endTime"
                type="time"
                value={formData.endTime}
                onChange={handleChange}
                error={!!formErrors.endTime}
                helperText={formErrors.endTime}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Box>

            <Box display="flex" flexDirection={{ xs: 'column', md: 'row' }} gap={2}>
              <TextField
                fullWidth
                label="연습 장소"
                name="location"
                value={formData.location}
                onChange={handleChange}
                error={!!formErrors.location}
                helperText={formErrors.location}
                required
              />
              <TextField
                fullWidth
                select
                label="팀"
                name="teamId"
                value={formData.teamId || ''}
                onChange={handleChange}
                SelectProps={{ native: true }}
              >
                <option value="">팀 선택</option>
                {teams.map((team) => (
                  <option key={team.id} value={team.id}>
                    {team.name}
                  </option>
                ))}
              </TextField>
            </Box>

            <TextField
              fullWidth
              label="연습 내용"
              name="content"
              value={formData.content}
              onChange={handleChange}
              error={!!formErrors.content}
              helperText={formErrors.content}
              multiline
              rows={4}
              required
            />

            <TextField
              fullWidth
              label="특이사항 및 코멘트"
              name="comment"
              value={formData.comment}
              onChange={handleChange}
              multiline
              rows={2}
            />

            <Box display="flex" gap={2}>
              <Button
                type="submit"
                variant="contained"
                disabled={loading}
              >
                {loading ? <CircularProgress size={LOADING_SPINNER_SIZE} /> : (isEdit ? '수정' : '등록')}
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/practices')}
                disabled={loading}
              >
                취소
              </Button>
            </Box>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default PracticeForm;
