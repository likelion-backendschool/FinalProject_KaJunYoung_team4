package com.finalProject.mutbook.web.controller.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.mutbook.app.util.Ut;
import com.finalProject.mutbook.domain.member.AuthMember;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.domain.product.Product;
import com.finalProject.mutbook.service.member.MemberService;
import com.finalProject.mutbook.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final String SECRET_KEY = "test_sk_4vZnjEJeQVx17vAMLZMrPmOoBN0k";

    private final OrderService orderService;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String doCreateOrder(RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal AuthMember authMember) {
        Order newOrder = orderService.createOrder(authMember.getMember());

        redirectAttributes.addAttribute("id", newOrder.getId());

        return "redirect:/order/{id}";
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showOrderList(Model model, @AuthenticationPrincipal AuthMember authMember) {

        List<Order> orderList = orderService.findAllByBuyer(authMember.getMember().getId());

        model.addAttribute("orders", orderList);

        return "order/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showOrderDetail(@PathVariable Long id, Model model,
                                  RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal AuthMember authMember) {

        Order newOrder = orderService.findByOrderId(id);

        if (newOrder == null) {
            redirectAttributes.addFlashAttribute("result", "잘못된 주문 번호입니다.");
            return "redirect:/order/list";
        }

        model.addAttribute("order", newOrder);

        return "order/detail";
    }

}
