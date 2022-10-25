package com.finalProject.mutbook.domain.member;

import com.finalProject.mutbook.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String email;

    private int authLevel;

    public Member(long id) {
        super(id);
    }

    public void changeBasicInfo(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
