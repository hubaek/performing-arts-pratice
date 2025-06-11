import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Grid,
  Alert,
  CircularProgress
} from '@mui/material';
import { practiceApi, teamApi, PracticeCreateRequest, Team } from '../../api';

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

  const [teams, setTeams] = useState<Team[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

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
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
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
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="연습 제목"
                name="title"
                value={formData.title}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="연습 날짜"
                name="practiceDate"
                type="date"
                value={formData.practiceDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Grid>

            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                label="시작 시간"
                name="startTime"
                type="time"
                value={formData.startTime}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Grid>

            <Grid item xs={12} md={3}>
              <TextField
                fullWidth
                label="종료 시간"
                name="endTime"
                type="time"
                value={formData.endTime}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="연습 장소"
                name="location"
                value={formData.location}
                onChange={handleChange}
                required
              />
            </Grid>

            <Grid item xs={12} md={6}>
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
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="연습 내용"
                name="content"
                value={formData.content}
                onChange={handleChange}
                multiline
                rows={4}
                required
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="특이사항 및 코멘트"
                name="comment"
                value={formData.comment}
                onChange={handleChange}
                multiline
                rows={2}
              />
            </Grid>

            <Grid item xs={12}>
              <Box display="flex" gap={2}>
                <Button
                  type="submit"
                  variant="contained"
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={20} /> : (isEdit ? '수정' : '등록')}
                </Button>
                <Button
                  variant="outlined"
                  onClick={() => navigate('/practices')}
                  disabled={loading}
                >
                  취소
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
};

export default PracticeForm;