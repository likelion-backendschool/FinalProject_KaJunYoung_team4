package com.finalProject.weekOne.web.dto.member.modify;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class ModifyPasswordDto {
    @NotBlank(message = "비밀번호에 공백을 포함할 수 없습니다!")
    private String oldPassword;

    @NotBlank(message = "비밀번호에 공백을 포함할 수 없습니다!")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 작성해주세요!")
    private String newPassword;
}
