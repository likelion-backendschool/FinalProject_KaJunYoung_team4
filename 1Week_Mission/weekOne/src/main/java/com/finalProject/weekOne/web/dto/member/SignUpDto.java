package com.finalProject.weekOne.web.dto.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
public class SignUpDto {
    @Size(min = 5, max = 15, message = "아이디는 5글자 ~ 15글자 이내로 작성해주세요!")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$", message = "아이디에 특수문자를 포함할 수 없습니다!")
    private String username;

    @NotBlank(message = "비밀번호에 공백을 포함할 수 없습니다!")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 작성해주세요!")
    private String password;

    private String nickname;

    @NotBlank(message = "이메일에 공백을 포함할 수 없습니다!")
    private String email;
}
