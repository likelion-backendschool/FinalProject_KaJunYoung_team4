package com.finalProject.mutbook.service.member;

import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.service.member.MemberService;
import com.finalProject.mutbook.web.dto.member.FindPwdDto;
import com.finalProject.mutbook.web.dto.member.modify.ModifyBaseInfoDto;
import com.finalProject.mutbook.web.dto.member.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void testBefore() {
        String username = "testUser";
        String password = "testUser1234!";
        String email = "testUser@test.com";

        SignUpDto dto = new SignUpDto();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setEmail(email);

        memberService.join(dto);
    }

    @Test
    @DisplayName("회원 정보 수정 (nickname, email)")
    void modifyBasicInfo() {
        // given
        Member foundMember = memberService.findByUsername("testUser");

        String email = "testUserChange@test.com";
        String nickname = "gomdori";

        ModifyBaseInfoDto dto = new ModifyBaseInfoDto();
        dto.setEmail(email);
        dto.setNickname(nickname);

        // when
        memberService.changeBasicInfo(foundMember.getUsername(), dto);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(email);
        assertThat(foundMember.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("회원 정보 수정 (password)")
    void modifyPassword() {
        // given
        Member foundMember = memberService.findByUsername("testUser");
        String rawPassword = "testUser1234!";
        String changePassword = "abcdefu1234!";

        // (given check)
        assertThat(passwordEncoder.matches(rawPassword, foundMember.getPassword())).isTrue();

        // when
        memberService.changeBasicInfo(foundMember.getUsername(), changePassword);

        // then
        assertThat(passwordEncoder.matches(changePassword, foundMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("아이디찾기")
    void findUsernameByEmail() {
        // given
        Member foundMember = memberService.findByUsername("testUser");
        String email = foundMember.getEmail();

        // when
        Member currentMember = memberService.findByEmail(email);

        // then
        assertThat(foundMember.getUsername()).isEqualTo(currentMember.getUsername());
    }

    @Test
    @DisplayName("비밀번호 찾기")
    void findMemberPassword() {
        // given
        Member foundMember = memberService.findByUsername("testUser");
        String username = "testUser";
        String email = "kawnsdud@gmail.com";

        FindPwdDto dto = FindPwdDto.builder()
                .username(username)
                .email(email)
                .build();

        // when
        memberService.sendFindPasswordMail(dto);

        // then
        String currentPwd = "testUser1234!";
        assertThat(passwordEncoder.matches(currentPwd, foundMember.getPassword())).isFalse();
    }

}