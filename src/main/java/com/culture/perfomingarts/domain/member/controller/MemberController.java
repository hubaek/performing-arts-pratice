package com.culture.perfomingarts.domain.member.controller;

import com.culture.perfomingarts.domain.member.dto.MemberCreateRequestDto;
import com.culture.perfomingarts.domain.member.dto.MemberListResponseDto;
import com.culture.perfomingarts.domain.member.dto.MemberPasswordChangeRequestDto;
import com.culture.perfomingarts.domain.member.dto.MemberResponseDto;
import com.culture.perfomingarts.domain.member.dto.MemberUpdateRequestDto;
import com.culture.perfomingarts.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberCreateRequestDto requestDto) {
        MemberResponseDto response = memberService.createMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {
        MemberResponseDto response = memberService.getMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponseDto> getMemberByEmail(@PathVariable String email) {
        MemberResponseDto response = memberService.getMemberByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MemberListResponseDto>> getAllActiveMembers() {
        List<MemberListResponseDto> members = memberService.getAllActiveMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MemberListResponseDto>> getMembersByTeam(@PathVariable Long teamId) {
        List<MemberListResponseDto> members = memberService.getMembersByTeam(teamId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<MemberListResponseDto>> getMembersByDepartment(@PathVariable String department) {
        List<MemberListResponseDto> members = memberService.getMembersByDepartment(department);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/join-year/{joinYear}")
    public ResponseEntity<List<MemberListResponseDto>> getMembersByJoinYear(@PathVariable Integer joinYear) {
        List<MemberListResponseDto> members = memberService.getMembersByJoinYear(joinYear);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberListResponseDto>> searchMembersByName(@RequestParam String name) {
        List<MemberListResponseDto> members = memberService.searchMembersByName(name);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = memberService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberUpdateRequestDto requestDto) {
        
        MemberResponseDto response = memberService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{memberId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberPasswordChangeRequestDto requestDto) {
        
        memberService.changePassword(memberId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{memberId}/activate")
    public ResponseEntity<Void> activateMember(@PathVariable Long memberId) {
        memberService.activateMember(memberId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{memberId}/deactivate")
    public ResponseEntity<Void> deactivateMember(@PathVariable Long memberId) {
        memberService.deactivateMember(memberId);
        return ResponseEntity.ok().build();
    }
}