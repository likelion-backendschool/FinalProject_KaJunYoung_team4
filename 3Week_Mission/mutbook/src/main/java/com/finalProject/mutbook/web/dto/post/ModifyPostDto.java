package com.finalProject.mutbook.web.dto.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter @Setter
public class ModifyPostDto {
    @Size(min = 4, max = 20, message = "제목은 4글자 ~ 20글자 이내로 작성해주세요!")
    private String subject;

    private String content;

    private String hashTagContents;
}