package com.finalProject.weekOne.web.controller.post;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.service.post.PostService;
import com.finalProject.weekOne.web.dto.post.CreatePostDto;
import com.finalProject.weekOne.web.dto.post.ModifyPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showPostList(Model model) {
        List<Post> posts = postService.findAllPost();
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showPostDetail(@PathVariable Long id, Model model) {
        Post currentPost = postService.findByPostId(id);
        model.addAttribute("post", currentPost);
        return "post/detail";
    }

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

    @GetMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String showPostUpdatePage(@PathVariable Long id, Model model) {
        Post currentPost = postService.findByPostId(id);
        model.addAttribute("post", currentPost);
        model.addAttribute("modifyPostDto", new ModifyPostDto());
        return "post/modify";
    }

    @PostMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String doPostUpdate(@Valid ModifyPostDto modifyPostDto,
                               BindingResult bindingResult, Model model,
                               @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modifyPostDto", modifyPostDto);
            return "post/write";
        }
        log.info("modifyPostDto.getSubject()={}", modifyPostDto.getSubject());
        log.info("modifyPostDto.getContent()={}", modifyPostDto.getContent());
        Post currentPost = postService.findByPostId(id);
        postService.modifyPost(currentPost, modifyPostDto);

        return "redirect:/post/{id}";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String doPostDelete(@PathVariable Long id,
                               @AuthenticationPrincipal AuthMember authMember,
                               RedirectAttributes redirectAttributes) {
        Post currentPost = postService.findByPostId(id);
        if (!currentPost.getAuthor().equals(authMember.getMember())) {
            redirectAttributes.addFlashAttribute("resultError", "삭제 권한이 없습니다.");
        }
        postService.removePost(currentPost);
        return "redirect:/post/list";
    }
}
