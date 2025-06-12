package com.culture.performingarts.config.security;

import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import com.culture.performingarts.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Spring Security에서 사용하는 UserDetailsService 구현체
 * 이메일을 기반으로 사용자 정보를 조회합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        
        return createUserDetails(member);
    }
    
    /**
     * Member 엔티티로부터 UserDetails 객체 생성
     */
    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(member.getRole().getAuthority())
        );
        
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(member.getStatus() != MemberStatus.ACTIVE)
                .build();
    }
}