package com.finalProject.weekOne.web.controller.home;

import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Post> posts = postService.findAllPost();
        model.addAttribute("posts", posts);
        return "home/main";
    }
}

