package com.finalProject.weekOne.service.member;

import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.member.MemberRepository;
import com.finalProject.weekOne.web.dto.member.ModifyDto;
import com.finalProject.weekOne.web.dto.member.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 새로운 회원을 저장하는 메소드
     * @param signUpDto 회원가입 시 입력된 정보를 담고 있음
     */
    public void join(SignUpDto signUpDto) {
        Member newMember = Member.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickname(signUpDto.getNickname())
                .email(signUpDto.getEmail())
                .createDate(LocalDateTime.now())
                .authLevel(3)
                .build();

        memberRepository.save(newMember);
    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void changeBasicInfo(Member currentMember, ModifyDto modifyDto) {
        currentMember.changeBasicInfo(modifyDto.getNickname(), modifyDto.getEmail());
    }

    @Transactional
    public void changePassword(Member currentMember, String password) {
        currentMember.changePassword(passwordEncoder.encode(password));
    }
}
