package com.finalProject.weekOne.web.controller.member;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.service.member.MemberService;
import com.finalProject.weekOne.web.dto.member.FindPwdDto;
import com.finalProject.weekOne.web.dto.member.ModifyDto;
import com.finalProject.weekOne.web.dto.member.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String showLoginPage() {
        return "member/login";
    }

    @GetMapping("/signUp")
    @PreAuthorize("isAnonymous()")
    public String showSignUpPage(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "member/signup";
    }

    @PostMapping("/signUp")
    @PreAuthorize("isAnonymous()")
    public String doSignup(@Valid SignUpDto signUpDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpDto", signUpDto);
            return "member/signup";
        }
        memberService.join(signUpDto);
        return "redirect:/member/login";
    }

    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String showModifyPage(Model model, @AuthenticationPrincipal AuthMember authMember) {
        Member currentMember = authMember.getMember();

        model.addAttribute("member", currentMember);

        return "member/modify";
    }

    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String doModifyBasicInfo(ModifyDto modifyDto, Model model, @AuthenticationPrincipal AuthMember authMember) {

        memberService.changeBasicInfo(authMember.getUsername(), modifyDto);

        return "redirect:/member/modify";
    }

    @GetMapping("/findUsername")
    @PreAuthorize("isAnonymous()")
    public String showFindUsernamePage() {

        return "member/findUsername";
    }

    @PostMapping("/findUsername")
    @PreAuthorize("isAnonymous()")
    public String doFindMemberByEmail(String email, RedirectAttributes redirectAttributes) {
        Member currentMember = memberService.findByEmail(email);
        redirectAttributes.addFlashAttribute("result", currentMember.getUsername());
        return "redirect:/member/findUsername";
    }

    @GetMapping("/findPassword")
    @PreAuthorize("isAnonymous()")
    public String showFindPasswordPage() {

        return "member/findPassword";
    }

    @PostMapping("/findPassword")
    @PreAuthorize("isAnonymous()")
    public String doFindPasswordByUsernameAndEmail(FindPwdDto findPwdDto, RedirectAttributes redirectAttributes) {
        String message = "메일함을 확인해주세요.(%s)".formatted(findPwdDto.getEmail());
        memberService.sendFindPasswordMail(findPwdDto);
        redirectAttributes.addFlashAttribute("result", message);
        return "redirect:/member/findPassword";
    }
}
