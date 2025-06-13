import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Alert,
  CircularProgress,
  Link,
  MenuItem,
  FormControl,
  InputLabel,
  Select
} from '@mui/material';
import { useAuth } from '../hooks/AuthContext';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { teamApi } from '../../team/api/teamApi';
import { TeamListItem } from '../../team/types';

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const MIN_PASSWORD_LENGTH = 6;
const LOADING_SPINNER_SIZE = 24;

// 형태 정의
interface SignupFormData {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber: string;
  birthDate: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major: string;
  department: string;
  position: string;
  responsibility: string;
  remarks: string;
  uniqueCode: string;
  teamId: string;
}

interface FormErrors {
  [key: string]: string;
}

// 통합 폼 검증 스키마
const validateSignupForm = (formData: SignupFormData) => {
  const errors: FormErrors = {};

  // 필수 필드 검증
  if (!formData.name) errors.name = '이름을 입력해주세요';
  if (!formData.email) errors.email = '이메일을 입력해주세요';
  if (!formData.password) errors.password = '비밀번호를 입력해주세요';
  if (!formData.birthDate) errors.birthDate = '생년월일을 입력해주세요';
  if (!formData.uniqueCode) errors.uniqueCode = '고유번호를 입력해주세요';

  // 이메일 형식 검증
  if (formData.email && !EMAIL_REGEX.test(formData.email)) {
    errors.email = '올바른 이메일 형식을 입력해주세요';
  }

  // 비밀번호 길이 검증
  if (formData.password && formData.password.length < MIN_PASSWORD_LENGTH) {
    errors.password = `비밀번호는 ${MIN_PASSWORD_LENGTH}자 이상이어야 합니다`;
  }

  // 생년월일 형식 검증
  if (formData.birthDate && !/^\d{4}-\d{2}-\d{2}$/.test(formData.birthDate)) {
    errors.birthDate = '올바른 날짜 형식을 입력해주세요 (YYYY-MM-DD)';
  }

  // 전화번호 형식 검증 (선택사항)
  if (formData.phoneNumber && !/^\d{3}-\d{4}-\d{4}$/.test(formData.phoneNumber)) {
    errors.phoneNumber = '올바른 전화번호 형식을 입력해주세요 (000-0000-0000)';
  }

  // 비밀번호 확인 검증
  if (formData.password !== formData.confirmPassword) {
    errors.confirmPassword = '비밀번호가 일치하지 않습니다';
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors
  };
};

const SignupForm: React.FC = () => {
  const navigate = useNavigate();
  const { signup, loading, error } = useAuth();
  const [formData, setFormData] = useState<SignupFormData>({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    birthDate: '',
    gender: 'MALE' as 'MALE' | 'FEMALE' | 'OTHER',
    joinYear: new Date().getFullYear(),
    major: '',
    department: '',
    position: '',
    responsibility: '',
    remarks: '',
    uniqueCode: '',
    teamId: ''
  });
  const [formErrors, setFormErrors] = useState<FormErrors>({});
  const [teams, setTeams] = useState<TeamListItem[]>([]);
  const [loadingTeams, setLoadingTeams] = useState(true);

  useEffect(() => {
    fetchActiveTeams();
  }, []);

  const fetchActiveTeams = async () => {
    try {
      const activeTeams = await teamApi.getActive();
      setTeams(activeTeams);
    } catch (err) {
      console.error('Error fetching active teams:', err);
    } finally {
      setLoadingTeams(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | React.ChangeEvent<{ name?: string; value: unknown }>) => {
    const name = e.target.name as string;
    const value = e.target.value;
    
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (formErrors[name]) {
      setFormErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = (): boolean => {
    const { isValid, errors } = validateSignupForm(formData);
    setFormErrors(errors);
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    try {
      const signupData = {
        ...formData,
        teamId: formData.teamId ? Number(formData.teamId) : undefined
      };
      
      await signup(signupData);
      navigate('/');
    } catch (error) {
      console.error('Signup failed:', error);
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
      bgcolor="#f5f5f5"
      py={4}
    >
      <Card sx={{ maxWidth: 600, width: '100%', mx: 2 }}>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h4" component="h1" gutterBottom align="center">
            회원가입
          </Typography>
          
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} noValidate>
            <TextField
              fullWidth
              label="이름"
              name="name"
              value={formData.name}
              onChange={handleChange}
              error={!!formErrors.name}
              helperText={formErrors.name}
              margin="normal"
              required
            />

            <TextField
              fullWidth
              label="이메일"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              error={!!formErrors.email}
              helperText={formErrors.email}
              margin="normal"
              required
            />

            <TextField
              fullWidth
              label="비밀번호"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              error={!!formErrors.password}
              helperText={formErrors.password}
              margin="normal"
              required
            />

            <TextField
              fullWidth
              label="비밀번호 확인"
              name="confirmPassword"
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange}
              error={!!formErrors.confirmPassword}
              helperText={formErrors.confirmPassword}
              margin="normal"
              required
            />

            <TextField
              fullWidth
              label="전화번호"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              error={!!formErrors.phoneNumber}
              helperText={formErrors.phoneNumber}
              margin="normal"
              placeholder="000-0000-0000"
            />

            <TextField
              fullWidth
              label="생년월일"
              name="birthDate"
              type="date"
              value={formData.birthDate}
              onChange={handleChange}
              error={!!formErrors.birthDate}
              helperText={formErrors.birthDate}
              margin="normal"
              required
              InputLabelProps={{
                shrink: true,
              }}
            />

            <FormControl fullWidth margin="normal">
              <InputLabel>성별</InputLabel>
              <Select
                name="gender"
                value={formData.gender}
                onChange={handleChange}
                label="성별"
              >
                <MenuItem value="MALE">남성</MenuItem>
                <MenuItem value="FEMALE">여성</MenuItem>
                <MenuItem value="OTHER">기타</MenuItem>
              </Select>
            </FormControl>

            <TextField
              fullWidth
              label="가입년도"
              name="joinYear"
              type="number"
              value={formData.joinYear}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="전공"
              name="major"
              value={formData.major}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="소속"
              name="department"
              value={formData.department}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="직책"
              name="position"
              value={formData.position}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="고유번호"
              name="uniqueCode"
              value={formData.uniqueCode}
              onChange={handleChange}
              error={!!formErrors.uniqueCode}
              helperText={formErrors.uniqueCode}
              margin="normal"
              required
            />

            <TextField
              fullWidth
              label="업무/담당"
              name="responsibility"
              value={formData.responsibility}
              onChange={handleChange}
              margin="normal"
            />

            <FormControl fullWidth margin="normal">
              <InputLabel>팀 선택</InputLabel>
              <Select
                name="teamId"
                value={formData.teamId}
                onChange={handleChange}
                label="팀 선택"
                disabled={loadingTeams}
              >
                <MenuItem value="">팀 선택 안함</MenuItem>
                {teams.map((team) => (
                  <MenuItem key={team.id} value={team.id.toString()}>
                    {team.name} ({team.memberCount}명)
                  </MenuItem>
                ))}
              </Select>
              {loadingTeams && (
                <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
                  팀 목록을 불러오는 중...
                </Typography>
              )}
            </FormControl>

            <TextField
              fullWidth
              label="비고"
              name="remarks"
              multiline
              rows={2}
              value={formData.remarks}
              onChange={handleChange}
              margin="normal"
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
              size="large"
            >
              {loading ? <CircularProgress size={LOADING_SPINNER_SIZE} /> : '회원가입'}
            </Button>

            <Box textAlign="center">
              <Typography variant="body2">
                이미 계정이 있으신가요?{' '}
                <Link component={RouterLink} to="/login" underline="hover">
                  로그인
                </Link>
              </Typography>
            </Box>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
};

export default SignupForm;