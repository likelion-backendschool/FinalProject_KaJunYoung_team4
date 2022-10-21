package com.finalProject.weekOne.web.controller.member;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.service.member.MemberService;
import com.finalProject.weekOne.web.dto.member.FindPwdDto;
import com.finalProject.weekOne.web.dto.member.modify.ModifyBaseInfoDto;
import com.finalProject.weekOne.web.dto.member.modify.ModifyPasswordDto;
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
        if (memberService.existByUsername(signUpDto.getUsername())) {
            model.addAttribute("signUpDto", signUpDto);
            model.addAttribute("usernameError", "이미 존재하는 아이디입니다.");
            return "member/signup";
        }
        if (memberService.existMemberEmail(signUpDto.getEmail())) {
            model.addAttribute("signUpDto", signUpDto);
            model.addAttribute("emailError", "이미 존재하는 이메일입니다.");
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
    public String doModifyBasicInfo(ModifyBaseInfoDto modifyBaseInfoDto, Model model, @AuthenticationPrincipal AuthMember authMember) {

        memberService.changeBasicInfo(authMember.getUsername(), modifyBaseInfoDto);

        return "redirect:/member/modify";
    }

    @GetMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String showModifyPasswordPage(Model model, @AuthenticationPrincipal AuthMember authMember) {
        Member currentMember = authMember.getMember();

        model.addAttribute("modifyPasswordDto", new ModifyPasswordDto());

        return "member/modifyPassword";
    }

    @PostMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String doModifyPassword(@Valid ModifyPasswordDto modifyPasswordDto, BindingResult bindingResult, Model model, @AuthenticationPrincipal AuthMember authMember, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || !memberService.checkMatchPassword(authMember.getUsername(), modifyPasswordDto.getOldPassword())) {
            model.addAttribute("modifyPasswordDto", modifyPasswordDto);
            model.addAttribute("resultError", "비밀번호를 확인해주세요.");
            return "member/modifyPassword";
        }

        memberService.changePassword(authMember.getUsername(), modifyPasswordDto.getNewPassword());
        redirectAttributes.addFlashAttribute("resultSuccess", "비밀번호가 변경되었습니다.");

        return "redirect:/member/modifyPassword";
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
