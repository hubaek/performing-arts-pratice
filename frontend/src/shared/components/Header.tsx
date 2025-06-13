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
  MenuItem,
  Divider,
  useTheme,
  useMediaQuery
} from '@mui/material';
import {
  AccountCircle as AccountCircleIcon,
  ExitToApp as LogoutIcon,
  Home as HomeIcon,
  List as ListIcon,
  AdminPanelSettings as AdminIcon,
  Menu as MenuIcon
} from '@mui/icons-material';
import { useAuth } from '../../domains/auth/hooks/AuthContext';

const Header: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuth();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [mobileMenuAnchorEl, setMobileMenuAnchorEl] = React.useState<null | HTMLElement>(null);

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
    setMobileMenuAnchorEl(null);
    navigate('/login');
  };

  const handleMobileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setMobileMenuAnchorEl(event.currentTarget);
  };

  const handleMobileMenuClose = () => {
    setMobileMenuAnchorEl(null);
  };

  const handleNavigation = (path: string) => {
    navigate(path);
    handleMenuClose();
    handleMobileMenuClose();
  };

  const isMenuOpen = Boolean(anchorEl);
  const isMobileMenuOpen = Boolean(mobileMenuAnchorEl);

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
            {!isMobile ? (
              <>
                <Button 
                  color="inherit" 
                  startIcon={<HomeIcon />}
                  onClick={() => navigate('/')}
                  sx={{ mr: 1 }}
                >
                  홈
                </Button>
                <Button 
                  color="inherit" 
                  startIcon={<ListIcon />}
                  onClick={() => navigate('/practices')}
                  sx={{ mr: 1 }}
                >
                  연습일지
                </Button>
                {user.role === 'ADMIN' && (
                  <Button 
                    color="inherit" 
                    startIcon={<AdminIcon />}
                    onClick={() => navigate('/admin/teams')}
                    sx={{ mr: 2 }}
                  >
                    관리자
                  </Button>
                )}
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
              </>
            ) : (
              <>
                <Typography variant="body2" sx={{ mr: 1 }}>
                  {user.name}님
                </Typography>
                <IconButton
                  size="large"
                  aria-label="메뉴 열기"
                  aria-controls="mobile-menu"
                  aria-haspopup="true"
                  onClick={handleMobileMenuOpen}
                  color="inherit"
                >
                  <MenuIcon />
                </IconButton>
              </>
            )}
            
            {/* 데스크톱 프로필 메뉴 */}
            {!isMobile && (
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
                <MenuItem onClick={() => handleNavigation('/')}>
                  <HomeIcon sx={{ mr: 1 }} />
                  홈
                </MenuItem>
                <MenuItem onClick={() => handleNavigation('/practices')}>
                  <ListIcon sx={{ mr: 1 }} />
                  연습일지
                </MenuItem>
                {user.role === 'ADMIN' && (
                  <MenuItem onClick={() => handleNavigation('/admin/teams')}>
                    <AdminIcon sx={{ mr: 1 }} />
                    관리자
                  </MenuItem>
                )}
                <Divider />
                <MenuItem onClick={handleLogout}>
                  <LogoutIcon sx={{ mr: 1 }} />
                  로그아웃
                </MenuItem>
              </Menu>
            )}
            
            {/* 모바일 메뉴 */}
            {isMobile && (
              <Menu
                id="mobile-menu"
                anchorEl={mobileMenuAnchorEl}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={isMobileMenuOpen}
                onClose={handleMobileMenuClose}
              >
                <MenuItem onClick={() => handleNavigation('/')}>
                  <HomeIcon sx={{ mr: 1 }} />
                  홈
                </MenuItem>
                <MenuItem onClick={() => handleNavigation('/practices')}>
                  <ListIcon sx={{ mr: 1 }} />
                  연습일지
                </MenuItem>
                {user.role === 'ADMIN' && (
                  <MenuItem onClick={() => handleNavigation('/admin/teams')}>
                    <AdminIcon sx={{ mr: 1 }} />
                    관리자
                  </MenuItem>
                )}
                <Divider />
                <MenuItem onClick={handleLogout}>
                  <LogoutIcon sx={{ mr: 1 }} />
                  로그아웃
                </MenuItem>
              </Menu>
            )}
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