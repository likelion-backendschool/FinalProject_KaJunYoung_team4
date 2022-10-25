package com.finalProject.mutbook.web.controller.cartItem;

import com.finalProject.mutbook.domain.cartItem.CartItem;
import com.finalProject.mutbook.domain.member.AuthMember;
import com.finalProject.mutbook.domain.product.Product;
import com.finalProject.mutbook.service.cartItem.CartItemService;
import com.finalProject.mutbook.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final ProductService productService;
    private final CartItemService cartItemService;

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String doAddCartItem(@PathVariable Long productId,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal AuthMember authMember) {
        Product product = productService.findByProductId(productId);
        cartItemService.addItem(authMember.getMember(), product);

        redirectAttributes.addFlashAttribute("result", "장바구니에 추가했습니다.(%s)".formatted(product.getSubject()));

        return "redirect:/product/{productId}";
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showCartItemList(@AuthenticationPrincipal AuthMember authMember, Model model) {
        List<CartItem> memberItems = cartItemService.findAllByBuyer(authMember.getMember());
        model.addAttribute("items", memberItems);
        return "cart/list";
    }
}
