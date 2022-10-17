package com.finalProject.weekOne.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {

        return "member/login";
    }
}
