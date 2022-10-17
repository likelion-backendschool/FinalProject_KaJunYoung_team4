package com.finalProject.weekOne.web.controller.member;

import com.finalProject.weekOne.service.member.MemberService;
import com.finalProject.weekOne.web.dto.member.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signUp")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "member/signup";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signUp")
    public String doSignup(@Valid SignUpDto signUpDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpDto", signUpDto);
            return "member/signup";
        }
        memberService.join(signUpDto);
        return "redirect:/member/login";
    }
}
