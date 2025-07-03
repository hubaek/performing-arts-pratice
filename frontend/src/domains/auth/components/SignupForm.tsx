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
  birthYear: string;
  birthMonth: string;
  birthDay: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
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
  if (!formData.phoneNumber) errors.phoneNumber = '전화번호를 입력해주세요';
  if (!formData.birthYear) errors.birthYear = '출생년도를 선택해주세요';
  if (!formData.birthMonth) errors.birthMonth = '출생월을 선택해주세요';
  if (!formData.birthDay) errors.birthDay = '출생일을 선택해주세요';
  if (!formData.uniqueCode) errors.uniqueCode = '고유번호를 입력해주세요';

  // 이메일 형식 검증
  if (formData.email && !EMAIL_REGEX.test(formData.email)) {
    errors.email = '올바른 이메일 형식을 입력해주세요';
  }

  // 비밀번호 길이 검증
  if (formData.password && formData.password.length < MIN_PASSWORD_LENGTH) {
    errors.password = `비밀번호는 ${MIN_PASSWORD_LENGTH}자 이상이어야 합니다`;
  }

  // 생년월일 유효성 검증
  if (formData.birthYear && formData.birthMonth && formData.birthDay) {
    const year = parseInt(formData.birthYear);
    const month = parseInt(formData.birthMonth);
    const day = parseInt(formData.birthDay);
    
    if (year < 1900 || year > new Date().getFullYear()) {
      errors.birthYear = '올바른 출생년도를 선택해주세요';
    }
    
    const date = new Date(year, month - 1, day);
    if (date.getFullYear() !== year || date.getMonth() !== month - 1 || date.getDate() !== day) {
      errors.birthDay = '존재하지 않는 날짜입니다';
    }
    
    if (date > new Date()) {
      errors.birthDay = '미래 날짜는 선택할 수 없습니다';
    }
  }

  // 전화번호 형식 검증
  if (formData.phoneNumber && !/^010-\d{4}-\d{4}$/.test(formData.phoneNumber)) {
    errors.phoneNumber = '올바른 전화번호 형식을 입력해주세요 (010-1234-1234)';
  }

  // 고유번호 형식 검증 (8자리-5자리)
  if (formData.uniqueCode && !/^\d{8}-\d{5}$/.test(formData.uniqueCode)) {
    errors.uniqueCode = '올바른 고유번호 형식을 입력해주세요 (00120314-00001)';
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
    birthYear: '',
    birthMonth: '',
    birthDay: '',
    gender: 'MALE' as 'MALE' | 'FEMALE' | 'OTHER',
    joinYear: new Date().getFullYear(),
    uniqueCode: '',
    teamId: ''
  });
  const [formErrors, setFormErrors] = useState<FormErrors>({});
  const [touchedFields, setTouchedFields] = useState<{[key: string]: boolean}>({});
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

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | any) => {
    const name = e.target.name as string;
    const value = e.target.value;
    
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // 이미 touched된 필드만 실시간 검증
    if (touchedFields[name]) {
      const newFormData = { ...formData, [name]: value };
      const { errors } = validateSignupForm(newFormData);
      setFormErrors(prev => ({
        ...prev,
        [name]: errors[name] || ''
      }));
    }
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const name = e.target.name as string;
    
    setTouchedFields(prev => ({
      ...prev,
      [name]: true
    }));
    
    // 필드 검증 수행
    const { errors } = validateSignupForm(formData);
    setFormErrors(prev => ({
      ...prev,
      [name]: errors[name] || ''
    }));
  };

  const validateForm = (): boolean => {
    const { isValid, errors } = validateSignupForm(formData);
    setFormErrors(errors);
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      alert('입력한 정보를 다시 확인해주세요.');
      return;
    }

    try {
      // 생년월일 조합
      const birthDate = `${formData.birthYear}-${formData.birthMonth.padStart(2, '0')}-${formData.birthDay.padStart(2, '0')}`;
      
      const signupData = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        phoneNumber: formData.phoneNumber,
        birthDate: birthDate,
        gender: formData.gender,
        joinYear: formData.joinYear,
        uniqueCode: formData.uniqueCode,
        teamId: formData.teamId ? Number(formData.teamId) : undefined
      };
      
      await signup(signupData);
      alert('회원가입이 완료되었습니다! 환영합니다.');
      navigate('/');
    } catch (error) {
      console.error('Signup failed:', error);
      alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
      bgcolor="#f5f5f5"
      py={2}
    >
      <Card sx={{ maxWidth: 600, width: '100%', mx: 2 }}>
        <CardContent sx={{ p: 3 }}>
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
              label={<span>이름 <span style={{ color: 'red' }}>*</span></span>}
              name="name"
              value={formData.name}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.name}
              helperText={formErrors.name}
              margin="dense"
              required
            />

            <TextField
              fullWidth
              label={<span>이메일 <span style={{ color: 'red' }}>*</span></span>}
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.email}
              helperText={formErrors.email}
              margin="dense"
              required
            />

            <TextField
              fullWidth
              label={<span>비밀번호 <span style={{ color: 'red' }}>*</span></span>}
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.password}
              helperText={formErrors.password}
              margin="dense"
              required
            />

            <TextField
              fullWidth
              label={<span>비밀번호 확인 <span style={{ color: 'red' }}>*</span></span>}
              name="confirmPassword"
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.confirmPassword}
              helperText={formErrors.confirmPassword}
              margin="dense"
              required
            />

            <TextField
              fullWidth
              label={<span>전화번호 <span style={{ color: 'red' }}>*</span></span>}
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.phoneNumber}
              helperText={formErrors.phoneNumber}
              margin="dense"
              placeholder="010-1234-1234"
              required
            />

            <Typography variant="body1" sx={{ mt: 1, mb: 0.5, fontWeight: 'medium' }}>
              생년월일 <span style={{ color: 'red' }}>*</span>
            </Typography>
            <Box sx={{ display: 'flex', gap: 1, mb: 1 }}>
              <FormControl sx={{ minWidth: 120 }} error={!!formErrors.birthYear}>
                <InputLabel>년도</InputLabel>
                <Select
                  name="birthYear"
                  value={formData.birthYear}
                  onChange={handleChange}
                  label="년도"
                >
                  {Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i).map(year => (
                    <MenuItem key={year} value={year.toString()}>{year}년</MenuItem>
                  ))}
                </Select>
                {formErrors.birthYear && (
                  <Typography variant="caption" color="error" sx={{ mt: 0.5, ml: 1.5 }}>
                    {formErrors.birthYear}
                  </Typography>
                )}
              </FormControl>
              
              <FormControl sx={{ minWidth: 80 }} error={!!formErrors.birthMonth}>
                <InputLabel>월</InputLabel>
                <Select
                  name="birthMonth"
                  value={formData.birthMonth}
                  onChange={handleChange}
                  label="월"
                >
                  {Array.from({ length: 12 }, (_, i) => i + 1).map(month => (
                    <MenuItem key={month} value={month.toString()}>{month}월</MenuItem>
                  ))}
                </Select>
                {formErrors.birthMonth && (
                  <Typography variant="caption" color="error" sx={{ mt: 0.5, ml: 1.5 }}>
                    {formErrors.birthMonth}
                  </Typography>
                )}
              </FormControl>
              
              <FormControl sx={{ minWidth: 80 }} error={!!formErrors.birthDay}>
                <InputLabel>일</InputLabel>
                <Select
                  name="birthDay"
                  value={formData.birthDay}
                  onChange={handleChange}
                  label="일"
                >
                  {Array.from({ length: 31 }, (_, i) => i + 1).map(day => (
                    <MenuItem key={day} value={day.toString()}>{day}일</MenuItem>
                  ))}
                </Select>
                {formErrors.birthDay && (
                  <Typography variant="caption" color="error" sx={{ mt: 0.5, ml: 1.5 }}>
                    {formErrors.birthDay}
                  </Typography>
                )}
              </FormControl>
            </Box>

            <FormControl fullWidth sx={{ mt: 1, mb: 1 }}>
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
              label="입과년도"
              name="joinYear"
              type="number"
              value={formData.joinYear}
              onChange={handleChange}
              margin="dense"
            />


            <TextField
              fullWidth
              label={<span>고유번호 <span style={{ color: 'red' }}>*</span></span>}
              name="uniqueCode"
              value={formData.uniqueCode}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.uniqueCode}
              helperText={formErrors.uniqueCode}
              margin="dense"
              placeholder="00120314-00001"
              required
            />


            <FormControl fullWidth sx={{ mt: 1, mb: 1 }}>
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


            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 2, mb: 1 }}
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