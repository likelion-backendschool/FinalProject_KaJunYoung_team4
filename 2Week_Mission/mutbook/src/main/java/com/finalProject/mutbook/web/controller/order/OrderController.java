package com.finalProject.mutbook.web.controller.order;

import com.finalProject.mutbook.domain.member.AuthMember;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.domain.product.Product;
import com.finalProject.mutbook.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String doCreateOrder(RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal AuthMember authMember) {
        Order newOrder = orderService.createOrder(authMember.getMember());

        redirectAttributes.addAttribute("id", newOrder.getId());

        return "redirect:/order/{id}";
    }

}
