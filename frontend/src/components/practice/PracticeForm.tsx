import React, { useState, useEffect } from 'react';
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
import { practiceApi, teamApi, PracticeCreateRequest, TeamListItem } from '../../api';

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

  useEffect(() => {
    loadTeams();
    if (isEdit) {
      loadPractice();
    }
  }, [id, isEdit]);

  const loadTeams = async () => {
    try {
      const data = await teamApi.getActive();
      setTeams(data);
    } catch (err) {
      console.error('Error loading teams:', err);
    }
  };

  const loadPractice = async () => {
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
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'teamId' ? (value ? Number(value) : undefined) : value
    }));
    
    // 입력 시 해당 필드 에러 제거
    if (formErrors[name]) {
      setFormErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

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
      if (isEdit) {
        await practiceApi.update(Number(id), formData);
      } else {
        await practiceApi.create(formData);
      }
      navigate('/practices');
    } catch (err) {
      setError(isEdit ? '수정에 실패했습니다.' : '등록에 실패했습니다.');
      console.error('Error saving practice:', err);
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
