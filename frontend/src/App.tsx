import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AppBar, Toolbar, Typography, Container } from '@mui/material';
import { AuthProvider } from './contexts/AuthContext';
import { LoginForm, SignupForm, ProtectedRoute, AdminRoute } from './components/auth';
import { PracticeList, PracticeForm, PracticeDetail } from './components/practice';
import { AdminTeamList } from './components/admin';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AuthProvider>
          <div className="App">
            <AppBar position="static">
              <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                  연습일지 관리 시스템
                </Typography>
              </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
              <Routes>
                <Route path="/login" element={<LoginForm />} />
                <Route path="/signup" element={<SignupForm />} />
                <Route path="/" element={
                  <ProtectedRoute>
                    <PracticeList />
                  </ProtectedRoute>
                } />
                <Route path="/practices" element={
                  <ProtectedRoute>
                    <PracticeList />
                  </ProtectedRoute>
                } />
                <Route path="/practices/new" element={
                  <ProtectedRoute>
                    <PracticeForm />
                  </ProtectedRoute>
                } />
                <Route path="/practices/:id" element={
                  <ProtectedRoute>
                    <PracticeDetail />
                  </ProtectedRoute>
                } />
                <Route path="/practices/:id/edit" element={
                  <ProtectedRoute>
                    <PracticeForm />
                  </ProtectedRoute>
                } />
                <Route path="/admin/teams" element={
                  <AdminRoute>
                    <AdminTeamList />
                  </AdminRoute>
                } />
              </Routes>
            </Container>
          </div>
        </AuthProvider>
      </Router>
    </ThemeProvider>
  );
}

export default App;