package com.finalProject.weekOne.service.member;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .createDate(LocalDateTime.now())
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

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void changeBasicInfo(String username, ModifyBaseInfoDto modifyBaseInfoDto) {
        Member currentMember = findByUsername(username);
        currentMember.changeBasicInfo(modifyBaseInfoDto.getNickname(), modifyBaseInfoDto.getEmail());
    }

    @Transactional
    public void changePassword(String username, String password) {
        Member currentMember = findByUsername(username);
        currentMember.changePassword(passwordEncoder.encode(password));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    public void sendFindPasswordMail(FindPwdDto dto) {
        if (!existMemberCheck(dto.getUsername(), dto.getEmail())) {
            return;
        }
        String tempPassword = getRandomPassword();
        changePassword(dto.getUsername(), tempPassword);
        MailDto mailDto = MailDto.builder()
                .title("[MUTBOOK] 비밀번호 찾기")
                .message(tempPassword)
                .email(dto.getEmail())
                .build();
        sendMail(mailDto);
    }

    public boolean existMemberCheck(String username, String email) {
        return memberRepository.existsByUsernameAndEmail(username, email);
    }

    public String getRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

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

    @Transactional(readOnly = true)
    public boolean checkMatchPassword(String username, String oldPassword) {
        Member currentMember = memberRepository.findByUsername(username).orElse(null);
        if (currentMember != null) {
            return passwordEncoder.matches(oldPassword, currentMember.getPassword());
        }
        return false;
    }
}
