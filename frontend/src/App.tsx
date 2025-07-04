import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { Container } from '@mui/material';
import { AuthProvider } from './domains/auth/hooks/AuthContext';
import { LoginForm, SignupForm, ProtectedRoute, AdminRoute } from './domains/auth';
import { PracticeList, PracticeForm, PracticeDetail } from './domains/practice';
import { AdminTeamList } from './domains/admin';
import { Dashboard } from './domains/dashboard';
import { Header, SnackbarProvider } from './shared/components';

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
      <SnackbarProvider>
        <Router>
          <AuthProvider>
            <div className="App">
              <Header />
              <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Routes>
                  <Route path="/login" element={<LoginForm />} />
                  <Route path="/signup" element={<SignupForm />} />
                  <Route path="/" element={
                    <ProtectedRoute>
                      <Dashboard />
                    </ProtectedRoute>
                  } />
                  <Route path="/dashboard" element={
                    <ProtectedRoute>
                      <Dashboard />
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
      </SnackbarProvider>
    </ThemeProvider>
  );
}

export default App;