import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/AuthContext';
import { Role } from '../../../shared/types';
import { Box, Typography, Paper } from '@mui/material';

interface AdminRouteProps {
  children: React.ReactNode;
}

const AdminRoute: React.FC<AdminRouteProps> = ({ children }) => {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <Typography>로딩 중...</Typography>
      </Box>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!user || user.role !== Role.ADMIN) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h5" color="error" gutterBottom>
            접근 권한이 없습니다
          </Typography>
          <Typography variant="body1" color="text.secondary">
            관리자만 접근할 수 있는 페이지입니다.
          </Typography>
        </Paper>
      </Box>
    );
  }

  return <>{children}</>;
};

export default AdminRoute;