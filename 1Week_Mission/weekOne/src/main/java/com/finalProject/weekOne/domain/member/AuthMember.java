package com.finalProject.weekOne.domain.member;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@JsonIncludeProperties({"id", "createDate", "modifyDate", "username", "nickname", "email", "authorities"})
public class AuthMember extends User {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String username;
    private final String nickname;
    private final String email;
    private final Set<GrantedAuthority> authorities;

    public AuthMember(Member member) {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        createDate = member.getCreateDate();
        modifyDate = member.getModifyDate();
        username = member.getUsername();
        nickname = member.getNickname();
        email = member.getEmail();
        authorities = new HashSet<>(member.getAuthorities());
    }
}