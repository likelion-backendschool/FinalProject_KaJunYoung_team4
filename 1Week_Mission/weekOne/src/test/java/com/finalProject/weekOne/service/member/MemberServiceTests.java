package com.finalProject.weekOne.service.member;

import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.web.dto.member.ModifyDto;
import com.finalProject.weekOne.web.dto.member.SignUpDto;
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

        ModifyDto dto = new ModifyDto();
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
        memberService.changePassword(foundMember, changePassword);

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

}