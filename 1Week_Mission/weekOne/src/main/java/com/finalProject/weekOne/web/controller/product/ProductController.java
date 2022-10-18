package com.finalProject.weekOne.web.controller.product;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.service.product.ProductService;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showPostList(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showPostDetail(@PathVariable Long id, Model model) {
        Product currentProduct = productService.findByProductId(id);
        model.addAttribute("product", currentProduct);
        return "product/detail";
    }
}
