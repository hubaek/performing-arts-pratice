import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authApi } from '../api';

export interface User {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  birthDate?: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  uniqueCode?: string;
  status: 'ACTIVE' | 'LEAVE_OF_ABSENCE' | 'INACTIVE';
  teamId?: number;
  createdAt: string;
  modifiedAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  memberId: number;
  name: string;
  email: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber?: string;
  birthDate: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  joinYear: number;
  major?: string;
  department?: string;
  position?: string;
  responsibility?: string;
  remarks?: string;
  uniqueCode?: string;
  teamId?: number;
}

interface AuthContextType {
  user: User | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  signup: (userData: SignupRequest) => Promise<void>;
  logout: () => void;
  refreshToken: () => Promise<void>;
  loading: boolean;
  error: string | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const isAuthenticated = !!user && !!accessToken;

  // 초기화 시 로컬 스토리지에서 토큰 확인
  useEffect(() => {
    const abortController = new AbortController();
    
    const initializeAuth = async () => {
      try {
        const storedToken = localStorage.getItem('accessToken');
        const storedRefreshToken = localStorage.getItem('refreshToken');
        
        if (storedToken && storedRefreshToken && !abortController.signal.aborted) {
          setAccessToken(storedToken);
          
          // 현재 사용자 정보 가져오기
          const userData = await authApi.getCurrentUser();
          if (!abortController.signal.aborted) {
            setUser(userData);
          }
        }
      } catch (error) {
        if (!abortController.signal.aborted) {
          console.error('Auth initialization failed:', error);
          // 토큰이 유효하지 않으면 제거
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
        }
      } finally {
        if (!abortController.signal.aborted) {
          setLoading(false);
        }
      }
    };

    initializeAuth();
    
    return () => {
      abortController.abort();
    };
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await authApi.login(credentials);
      
      // 토큰 저장
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      
      setAccessToken(response.accessToken);
      
      // 사용자 정보 다시 조회하여 전체 정보 설정
      const userData = await authApi.getCurrentUser();
      setUser(userData);
    } catch (error: unknown) {
      const errorMessage = error instanceof Error && 'response' in error 
        ? (error as any).response?.data?.message || '로그인에 실패했습니다.'
        : '로그인에 실패했습니다.';
      setError(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const signup = async (userData: SignupRequest) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await authApi.signup(userData);
      
      // 회원가입 후 자동 로그인
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      
      setAccessToken(response.accessToken);
      
      // 사용자 정보 다시 조회하여 전체 정보 설정
      const userInfo = await authApi.getCurrentUser();
      setUser(userInfo);
    } catch (error: unknown) {
      const errorMessage = error instanceof Error && 'response' in error 
        ? (error as any).response?.data?.message || '회원가입에 실패했습니다.'
        : '회원가입에 실패했습니다.';
      setError(errorMessage);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setAccessToken(null);
    setUser(null);
    setError(null);
  };

  const refreshToken = async () => {
    try {
      const storedRefreshToken = localStorage.getItem('refreshToken');
      if (!storedRefreshToken) {
        throw new Error('No refresh token available');
      }

      const response = await authApi.refreshToken({ refreshToken: storedRefreshToken });
      
      localStorage.setItem('accessToken', response.accessToken);
      setAccessToken(response.accessToken);
      
      // 사용자 정보 다시 조회하여 전체 정보 설정
      const userData = await authApi.getCurrentUser();
      setUser(userData);
    } catch (error) {
      console.error('Token refresh failed:', error);
      logout();
      throw error;
    }
  };

  const value: AuthContextType = {
    user,
    accessToken,
    isAuthenticated,
    login,
    signup,
    logout,
    refreshToken,
    loading,
    error
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};