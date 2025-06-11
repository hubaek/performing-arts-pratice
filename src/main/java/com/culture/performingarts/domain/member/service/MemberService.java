package com.culture.performingarts.domain.member.service;

import com.culture.performingarts.domain.member.dto.MemberCreateRequestDto;
import com.culture.performingarts.domain.member.dto.MemberListResponseDto;
import com.culture.performingarts.domain.member.dto.MemberPasswordChangeRequestDto;
import com.culture.performingarts.domain.member.dto.MemberResponseDto;
import com.culture.performingarts.domain.member.dto.MemberUpdateRequestDto;
import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import com.culture.performingarts.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto createMember(MemberCreateRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword()) // TODO: 암호화 필요
                .phoneNumber(requestDto.getPhoneNumber())
                .birthDate(requestDto.getBirthDate())
                .gender(requestDto.getGender())
                .joinYear(requestDto.getJoinYear())
                .major(requestDto.getMajor())
                .department(requestDto.getDepartment())
                .position(requestDto.getPosition())
                .responsibility(requestDto.getResponsibility())
                .remarks(requestDto.getRemarks())
                .uniqueCode(requestDto.getUniqueCode())
                .status(requestDto.getStatus())
                .teamId(requestDto.getTeamId())
                .build();

        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.from(savedMember);
    }

    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        
        return MemberResponseDto.from(member);
    }

    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        
        return MemberResponseDto.from(member);
    }

    public List<MemberListResponseDto> getAllActiveMembers() {
        return memberRepository.findByStatus(MemberStatus.ACTIVE)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberListResponseDto> getMembersByTeam(Long teamId) {
        return memberRepository.findByTeamIdAndStatus(teamId, MemberStatus.ACTIVE)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberListResponseDto> getMembersByDepartment(String department) {
        return memberRepository.findByDepartmentAndStatus(department, MemberStatus.ACTIVE)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberListResponseDto> getMembersByJoinYear(Integer joinYear) {
        return memberRepository.findByJoinYearAndStatus(joinYear, MemberStatus.ACTIVE)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberListResponseDto> searchMembersByName(String name) {
        return memberRepository.searchByNameAndStatus(name, MemberStatus.ACTIVE)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.updateInfo(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getDepartment(),
                requestDto.getPosition(),
                requestDto.getResponsibility(),
                requestDto.getRemarks(),
                requestDto.getTeamId()
        );

        return MemberResponseDto.from(member);
    }

    @Transactional
    public void changePassword(Long memberId, MemberPasswordChangeRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // TODO: 현재 비밀번호 확인 로직 추가 필요
        // TODO: 비밀번호 암호화 로직 추가 필요
        
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        member.changePassword(requestDto.getNewPassword());
    }

    @Transactional
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.deactivate();
    }

    @Transactional
    public void activateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.activate();
    }

    @Transactional
    public void changeMemberStatus(Long memberId, MemberStatus status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.changeStatus(status);
    }

    @Transactional
    public void putMemberOnLeave(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        member.takeLeaveOfAbsence();
    }

    public List<MemberListResponseDto> getMembersByStatus(MemberStatus status) {
        return memberRepository.findByStatus(status)
                .stream()
                .map(MemberListResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MemberListResponseDto> getLeaveOfAbsenceMembers() {
        return getMembersByStatus(MemberStatus.LEAVE_OF_ABSENCE);
    }

    public List<MemberListResponseDto> getInactiveMembers() {
        return getMembersByStatus(MemberStatus.INACTIVE);
    }

    public MemberResponseDto getMemberByUniqueCode(String uniqueCode) {
        Member member = memberRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        
        return MemberResponseDto.from(member);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean existsByUniqueCode(String uniqueCode) {
        return memberRepository.existsByUniqueCode(uniqueCode);
    }
}