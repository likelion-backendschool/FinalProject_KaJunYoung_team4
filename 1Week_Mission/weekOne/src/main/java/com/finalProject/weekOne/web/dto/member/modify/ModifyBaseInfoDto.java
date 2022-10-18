package com.finalProject.weekOne.web.dto.member.modify;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ModifyBaseInfoDto {
    @NotBlank(message = "닉네임에 공백을 포함할 수 없습니다!")
    private String nickname;

    @NotBlank(message = "이메일에 공백을 포함할 수 없습니다!")
    private String email;
}
