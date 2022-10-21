package com.finalProject.weekOne.web.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@Builder
public class FindPwdDto {
    @NotBlank(message = "닉네임에 공백을 포함할 수 없습니다!")
    private String username;

    @NotBlank(message = "이메일에 공백을 포함할 수 없습니다!")
    private String email;
}
