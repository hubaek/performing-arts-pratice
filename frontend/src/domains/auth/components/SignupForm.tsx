import React, { useState, useEffect } from 'react';
import {
  Box,
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
import { useSnackbar } from '../../../shared/components';
import { textFieldStyle, formControlStyle, primaryButtonStyle, cardContainerStyle, titleStyle } from '../../../shared/styles/formStyles';

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
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    
    // 년도 범위 검증
    if (year < 1900 || year > currentYear) {
      errors.birthYear = '올바른 출생년도를 선택해주세요';
    }
    
    // 유효한 날짜인지 검증
    const inputDate = new Date(year, month - 1, day);
    const isValidDate = inputDate.getFullYear() === year && 
                       inputDate.getMonth() === month - 1 && 
                       inputDate.getDate() === day;
    
    if (!isValidDate) {
      errors.birthDay = '존재하지 않는 날짜입니다';
    }
    
    // 미래 날짜 검증
    if (isValidDate && inputDate > currentDate) {
      errors.birthDay = '미래 날짜는 선택할 수 없습니다';
    }
    
    // 너무 오래된 날짜 검증 (120세 이상)
    const minDate = new Date(currentYear - 120, 0, 1);
    if (isValidDate && inputDate < minDate) {
      errors.birthYear = '올바른 출생년도를 선택해주세요';
    }
    
    // 윤년 처리 - 2월 29일 검증
    if (month === 2 && day === 29) {
      const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
      if (!isLeapYear) {
        errors.birthDay = '해당 년도에는 2월 29일이 없습니다';
      }
    }
  }

  // 전화번호 형식 검증 (더 유연한 형식 지원)
  if (formData.phoneNumber) {
    // 숫자만 추출
    const numbersOnly = formData.phoneNumber.replace(/\D/g, '');
    
    // 010으로 시작하는 11자리 또는 02/031/032 등으로 시작하는 지역번호
    const mobilePattern = /^010\d{8}$/;
    const landlinePattern = /^(02|0[3-6]\d)\d{7,8}$/;
    
    if (!mobilePattern.test(numbersOnly) && !landlinePattern.test(numbersOnly)) {
      errors.phoneNumber = '올바른 전화번호를 입력해주세요 (예: 010-1234-1234, 02-1234-5678)';
    }
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
  const { showSnackbar } = useSnackbar();
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
      showSnackbar('입력한 정보를 다시 확인해주세요.', 'error');
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
      showSnackbar('회원가입이 완료되었습니다! 환영합니다.', 'success');
      navigate('/');
    } catch (error) {
      console.error('Signup failed:', error);
      showSnackbar('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.', 'error');
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="flex-start"
      minHeight="100vh"
      bgcolor="#f8f9fa"
      py={4}
    >
      <Box sx={cardContainerStyle}>
          <Typography 
            variant="h4" 
            component="h1" 
            gutterBottom 
            align="center"
            sx={titleStyle}
          >
            회원가입
          </Typography>
          
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              fullWidth
              label="이름"
              name="name"
              value={formData.name}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.name}
              helperText={formErrors.name}
              margin="normal"
              required
              sx={textFieldStyle}
            />

            <TextField
              fullWidth
              label="이메일"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.email}
              helperText={formErrors.email}
              margin="normal"
              sx={textFieldStyle}
              required
            />

            <TextField
              fullWidth
              label="비밀번호"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.password}
              helperText={formErrors.password}
              margin="normal"
              sx={textFieldStyle}
              required
            />

            <TextField
              fullWidth
              label="비밀번호 확인"
              name="confirmPassword"
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.confirmPassword}
              helperText={formErrors.confirmPassword}
              margin="normal"
              sx={textFieldStyle}
              required
            />

            <TextField
              fullWidth
              label="전화번호"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.phoneNumber}
              helperText={formErrors.phoneNumber}
              margin="normal"
              sx={textFieldStyle}
              placeholder="예: 010-1234-1234 또는 02-1234-5678"
              required
            />

            <Typography 
              variant="body1" 
              sx={{ 
                mt: 1, 
                mb: 1.5, 
                fontWeight: 600,
                fontSize: '15px',
                color: '#333'
              }}
            >
              생년월일
            </Typography>
            <Box sx={{ display: 'flex', gap: 1.5, mb: 2.5 }}>
              <FormControl 
                sx={{ 
                  minWidth: 120,
                  ...formControlStyle
                }} 
                error={!!formErrors.birthYear}
              >
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
              
              <FormControl 
                sx={{ 
                  minWidth: 80,
                  ...formControlStyle
                }} 
                error={!!formErrors.birthMonth}
              >
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
              
              <FormControl 
                sx={{ 
                  minWidth: 80,
                  ...formControlStyle
                }} 
                error={!!formErrors.birthDay}
              >
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

            <FormControl 
              fullWidth 
              sx={{ 
                mt: 2, 
                mb: 2,
                ...formControlStyle
              }}
            >
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
              margin="normal"
              sx={textFieldStyle}
            />


            <TextField
              fullWidth
              label="고유번호"
              name="uniqueCode"
              value={formData.uniqueCode}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!formErrors.uniqueCode}
              helperText={formErrors.uniqueCode}
              margin="normal"
              sx={textFieldStyle}
              placeholder="00120314-00001"
              required
            />


            <FormControl 
              fullWidth 
              sx={{ 
                mt: 2, 
                mb: 2,
                ...formControlStyle
              }}
            >
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
              disabled={loading}
              size="large"
              sx={{
                mt: 3,
                mb: 3,
                ...primaryButtonStyle
              }}
            >
              {loading ? <CircularProgress size={LOADING_SPINNER_SIZE} color="inherit" /> : '회원가입'}
            </Button>

            <Box textAlign="center" sx={{ mt: 2 }}>
              <Typography variant="body2" sx={{ color: '#666' }}>
                이미 계정이 있으신가요?{' '}
                <Link 
                  component={RouterLink} 
                  to="/login" 
                  underline="hover"
                  sx={{ 
                    color: '#1976d2',
                    fontWeight: 500
                  }}
                >
                  로그인
                </Link>
              </Typography>
            </Box>
          </Box>
      </Box>
    </Box>
  );
};

export default SignupForm;