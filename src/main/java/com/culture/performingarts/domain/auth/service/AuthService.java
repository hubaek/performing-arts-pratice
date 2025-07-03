package com.culture.performingarts.domain.auth.service;

import com.culture.performingarts.config.security.JwtTokenProvider;
import com.culture.performingarts.domain.auth.dto.AuthResponseDto;
import com.culture.performingarts.domain.auth.dto.LoginRequestDto;
import com.culture.performingarts.domain.auth.dto.SignupRequestDto;
import com.culture.performingarts.domain.auth.dto.TokenRefreshRequestDto;
import com.culture.performingarts.domain.auth.exception.InvalidLoginCredentialsException;
import com.culture.performingarts.domain.auth.exception.InvalidTokenException;
import com.culture.performingarts.domain.auth.exception.InvalidMemberStatusException;
import com.culture.performingarts.domain.auth.exception.UniqueCodeDuplicateException;
import com.culture.performingarts.global.exception.PasswordMismatchException;
import com.culture.performingarts.global.exception.UnauthorizedException;
import com.culture.performingarts.global.util.SecurityMaskingUtil;
import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import com.culture.performingarts.domain.member.exception.EmailDuplicateException;
import com.culture.performingarts.domain.member.exception.MemberNotFoundException;
import com.culture.performingarts.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    
    /**
     * 로그인 처리
     */
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 회원 정보 조회
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
        
        // 활성 상태가 아닌 회원은 로그인 불가
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new InvalidMemberStatusException("활동 중이 아닌 회원입니다. 관리자에게 문의하세요");
        }
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        
        log.info("User logged in successfully - email: {}", SecurityMaskingUtil.maskEmail(member.getEmail()));
        
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
    
    /**
     * 회원가입 처리
     */
    @Transactional
    public AuthResponseDto signup(SignupRequestDto signupRequest) {
        // 비밀번호 일치 확인
        if (!signupRequest.isPasswordMatching()) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다");
        }
        
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailDuplicateException("이미 사용 중인 이메일입니다");
        }
        
        // 고유코드 중복 확인 (고유코드가 있는 경우)
        if (signupRequest.getUniqueCode() != null && 
            memberRepository.existsByUniqueCode(signupRequest.getUniqueCode())) {
            throw new UniqueCodeDuplicateException("이미 사용 중인 고유번호입니다");
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        
        // 회원 생성
        Member member = Member.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(encodedPassword)
                .phoneNumber(signupRequest.getPhoneNumber())
                .birthDate(signupRequest.getBirthDate())
                .gender(signupRequest.getGender())
                .joinYear(signupRequest.getJoinYear())
                .major(signupRequest.getMajor())
                .department(signupRequest.getDepartment())
                .position(signupRequest.getPosition())
                .responsibility(signupRequest.getResponsibility())
                .remarks(signupRequest.getRemarks())
                .uniqueCode(signupRequest.getUniqueCode())
                .status(MemberStatus.ACTIVE)
                .teamId(signupRequest.getTeamId())
                .build();
        
        Member savedMember = memberRepository.save(member);
        
        // 자동 로그인을 위한 인증 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signupRequest.getEmail(),
                signupRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        
        log.info("User signed up successfully - email: {}", SecurityMaskingUtil.maskEmail(savedMember.getEmail()));
        
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }
    
    /**
     * 토큰 갱신 처리
     */
    public AuthResponseDto refreshToken(TokenRefreshRequestDto tokenRefreshRequest) {
        String refreshToken = tokenRefreshRequest.getRefreshToken();
        
        // 리프레시 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 리프레시 토큰입니다");
        }
        
        // 리프레시 토큰에서 사용자 정보 추출
        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
        
        // 활성 상태 확인
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new InvalidMemberStatusException("활동 중이 아닌 회원입니다");
        }
        
        // 새로운 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            email, null, null
        );
        
        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        
        log.info("Token refreshed successfully - email: {}", SecurityMaskingUtil.maskEmail(member.getEmail()));
        
        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // 기존 리프레시 토큰 재사용
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
    
    /**
     * 현재 인증된 사용자의 회원 정보 조회
     */
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다");
        }
        
        String email = authentication.getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다"));
    }
}