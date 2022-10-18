package com.finalProject.weekOne.web.controller.post;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.service.post.PostService;
import com.finalProject.weekOne.web.dto.post.CreatePostDto;
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

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String showPostWritePage(Model model) {
        model.addAttribute("createPostDto", new CreatePostDto());
        return "post/write";
    }

    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String doPostWrite(@Valid CreatePostDto createPostDto,
                              BindingResult bindingResult, Model model,
                              @AuthenticationPrincipal AuthMember authMember) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createPostDto", createPostDto);
            return "post/write";
        }
        postService.savePost(authMember.getMember(), createPostDto);

        return "post/write";
    }
}
