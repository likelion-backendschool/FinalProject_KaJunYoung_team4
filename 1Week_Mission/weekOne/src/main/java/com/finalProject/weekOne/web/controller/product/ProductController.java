package com.finalProject.weekOne.web.controller.product;

import com.finalProject.weekOne.domain.member.AuthMember;
import com.finalProject.weekOne.service.product.ProductService;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import com.finalProject.weekOne.web.dto.product.ModifyProductDto;
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
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String showProductWritePage(Model model) {
        model.addAttribute("createProductDto", new CreateProductDto());
        return "product/write";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String doProductWrite(@Valid CreateProductDto createProductDto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AuthMember authMember) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "product/write";
        }
        Product savedProduct = productService.saveProduct(authMember.getMember(), createProductDto);
        redirectAttributes.addAttribute("id", savedProduct.getId());
        return "redirect:/product/{id}";
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showProductList(Model model) {
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product currentProduct = productService.findByProductId(id);
        model.addAttribute("product", currentProduct);
        return "product/detail";
    }

    @GetMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String showProductUpdatePage(@PathVariable Long id, Model model) {
        Product currentProduct = productService.findByProductId(id);
        model.addAttribute("product", currentProduct);
        model.addAttribute("modifyProductDto", new ModifyProductDto());
        return "product/modify";
    }

    @PostMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String doProductUpdate(@Valid ModifyProductDto modifyProductDto,
                               BindingResult bindingResult, Model model,
                               @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modifyProductDto", modifyProductDto);
            return "product/write";
        }
        Product currentProduct = productService.findByProductId(id);
        productService.modifyProduct(currentProduct, modifyProductDto);

        return "redirect:/product/{id}";
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String doProductDelete(@PathVariable Long id,
                               @AuthenticationPrincipal AuthMember authMember,
                               RedirectAttributes redirectAttributes) {
        Product currentProduct = productService.findByProductId(id);
        if (!currentProduct.getAuthor().equals(authMember.getMember())) {
            redirectAttributes.addFlashAttribute("resultError", "삭제 권한이 없습니다.");
        }
        productService.removeProduct(currentProduct);
        return "redirect:/product/list";
    }
}
