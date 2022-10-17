package com.finalProject.weekOne.domain.member;

import com.finalProject.weekOne.domain.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    @Column(unique = true)
    private String nickname;

    private int authLevel;

    public Member(long id) {
        super(id);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (authLevel == 3) {
            authorities.add(new SimpleGrantedAuthority("MEMBER"));
        } else if (authLevel == 7) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        return authorities;
    }

}