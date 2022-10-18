package com.finalProject.weekOne.web.controller.product;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.service.product.ProductService;
import com.finalProject.weekOne.web.dto.post.CreatePostDto;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String showPostWritePage(Model model) {
        model.addAttribute("createProductDto", new CreateProductDto());
        return "product/write";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String doPostWrite(@Valid CreateProductDto createProductDto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AuthMember authMember) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "post/write";
        }
        Product savedProduct = productService.saveProduct(authMember.getMember(), createProductDto);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        return "redirect:/product/{id}";
    }
}
