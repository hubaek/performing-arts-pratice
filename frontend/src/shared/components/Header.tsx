import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem
} from '@mui/material';
import {
  AccountCircle as AccountCircleIcon,
  ExitToApp as LogoutIcon
} from '@mui/icons-material';
import { useAuth } from '../../domains/auth/hooks/AuthContext';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuth();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const handleHomeClick = () => {
    navigate('/');
  };

  const handleProfileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    handleMenuClose();
    navigate('/login');
  };

  const isMenuOpen = Boolean(anchorEl);

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography 
          variant="h6" 
          component="div" 
          sx={{ 
            flexGrow: 1, 
            cursor: 'pointer',
            '&:hover': {
              opacity: 0.8
            }
          }}
          onClick={handleHomeClick}
        >
          연습일지 관리 시스템
        </Typography>
        
        {isAuthenticated && user && (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Typography variant="body1" sx={{ mr: 2 }}>
              {user.name}님
            </Typography>
            <IconButton
              size="large"
              aria-label="사용자 계정"
              aria-controls="profile-menu"
              aria-haspopup="true"
              onClick={handleProfileMenuOpen}
              color="inherit"
            >
              <AccountCircleIcon />
            </IconButton>
            <Menu
              id="profile-menu"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={isMenuOpen}
              onClose={handleMenuClose}
            >
              <MenuItem onClick={handleLogout}>
                <LogoutIcon sx={{ mr: 1 }} />
                로그아웃
              </MenuItem>
            </Menu>
          </Box>
        )}
        
        {!isAuthenticated && (
          <Box>
            <Button color="inherit" onClick={() => navigate('/login')}>
              로그인
            </Button>
            <Button color="inherit" onClick={() => navigate('/signup')}>
              회원가입
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Header;