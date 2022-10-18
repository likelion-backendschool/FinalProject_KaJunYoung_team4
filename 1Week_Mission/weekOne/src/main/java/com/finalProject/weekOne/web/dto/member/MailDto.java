package com.finalProject.weekOne.web.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MailDto {
    private String email;
    private String title;
    private String message;
}
