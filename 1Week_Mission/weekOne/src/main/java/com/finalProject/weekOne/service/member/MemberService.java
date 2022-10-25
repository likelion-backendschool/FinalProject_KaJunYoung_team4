package com.finalProject.weekOne.service.member;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.member.MemberRepository;
import com.finalProject.weekOne.web.dto.member.FindPwdDto;
import com.finalProject.weekOne.web.dto.member.MailDto;
import com.finalProject.weekOne.web.dto.member.modify.ModifyBaseInfoDto;
import com.finalProject.weekOne.web.dto.member.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String FROM;

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
                .authLevel(3)
                .build();

        MailDto mailDto = MailDto.builder()
                .title("[MUTBOOK] 회원가입 완료")
                .message("회원가입을 축하합니다.")
                .email(signUpDto.getEmail())
                .build();
        sendMail(mailDto);

        memberRepository.save(newMember);
    }

    /**
     * Username을 통해 Member 객체를 찾는 메소드
     * @param username Member의 로그인 아이디
     */
    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    /**
     * 필명, 이메일을 바꾸는 메소드
     * @param username Member의 로그인 아이디
     * @param modifyBaseInfoDto 변경할 필명, 이메일이 담긴 DTO
     */
    @Transactional
    public void changeBasicInfo(String username, ModifyBaseInfoDto modifyBaseInfoDto) {
        Member currentMember = findByUsername(username);
        currentMember.changeBasicInfo(modifyBaseInfoDto.getNickname(), modifyBaseInfoDto.getEmail());
        forceAuthentication(currentMember);
    }

    /**
     * 비밀번호를 바꾸는 메소드
     * @param username Member의 로그인 아이디
     * @param password 변경할 비밀번호
     */
    @Transactional
    public void changeBasicInfo(String username, String password) {
        Member currentMember = findByUsername(username);
        currentMember.changePassword(passwordEncoder.encode(password));
        forceAuthentication(currentMember);
    }

    /**
     * 이메일을 통해 Member 객체를 찾는 메소드
     * @param email 가입 시 입력한 Email
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    /**
     * 비밀번호를 찾기 위한 메소드
     * @param findPwdDto 비밀번호 찾기에 필요한 아이디와 이메일이 담긴 DTO
     */
    public void sendFindPasswordMail(FindPwdDto findPwdDto) {
        if (!existMemberCheck(findPwdDto.getUsername(), findPwdDto.getEmail())) {
            return;
        }
        String tempPassword = getRandomPassword();
        changeBasicInfo(findPwdDto.getUsername(), tempPassword);
        MailDto mailDto = MailDto.builder()
                .title("[MUTBOOK] 비밀번호 찾기")
                .message(tempPassword)
                .email(findPwdDto.getEmail())
                .build();
        sendMail(mailDto);
    }

    /**
     * 아이디와 이메일이 일치하는 Member 객체가 있는지 조회
     * @param username Member의 로그인 아이디
     * @param email 가입 시 입력한 Email
     */
    public boolean existMemberCheck(String username, String email) {
        return memberRepository.existsByUsernameAndEmail(username, email);
    }

    /**
     * 이메일이 일치하는 Member 객체가 있는지 조회
     * @param email 회원가입 시 입력한 Email
     */
    public boolean existMemberEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 이메일이 일치하는 Member 객체가 있는지 조회
     * @param username 회원가입 시 입력한 로그인 아이디
     */
    public boolean existByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    /**
     * 랜덤 비밀번호를 UUID를 통해 15자리 String을 생성
     */
    public String getRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    /**
     * 이메일을 보내는 메소드
     * @param mailDto 받는 사람의 이메일, 제목, 내용을 담는 DTO
     */
    public void sendMail(MailDto mailDto) {
        SimpleMailMessage sm = new SimpleMailMessage();
        try {
            sm.setTo(mailDto.getEmail());
            sm.setFrom(FROM);
            sm.setSubject(mailDto.getTitle());
            sm.setText(mailDto.getMessage());
            javaMailSender.send(sm);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    /**
     * 비밀번호 변경을 위해 현재 비밀번호와 맞는지 비교하는 메소드
     * @param username 현재 유저의 로그인 아이디
     * @param oldPassword 기존 로그인 비밀번호
     */
    @Transactional(readOnly = true)
    public boolean checkMatchPassword(String username, String oldPassword) {
        Member currentMember = memberRepository.findByUsername(username).orElse(null);
        if (currentMember != null) {
            return passwordEncoder.matches(oldPassword, currentMember.getPassword());
        }
        return false;
    }

    public void forceAuthentication (Member member) {
        String memberRole = "";
        if (member.getAuthLevel() == 3) {
            memberRole = "MEMBER";
        } else {
            memberRole = "ADMIN";
        }
        AuthMember authMember = new AuthMember(member, Collections.singletonList(new SimpleGrantedAuthority(memberRole)));

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        authMember,
                        null,
                        authMember.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
