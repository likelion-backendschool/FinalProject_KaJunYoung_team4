package com.finalProject.mutbook.web.controller.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.mutbook.app.util.Ut;
import com.finalProject.mutbook.domain.member.AuthMember;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.service.member.MemberService;
import com.finalProject.mutbook.service.order.OrderService;
import com.finalProject.mutbook.web.controller.order.exception.ActorCanNotPayOrderException;
import com.finalProject.mutbook.web.controller.order.exception.OrderIdNotMatchedException;
import com.finalProject.mutbook.web.controller.order.exception.OrderNotEnoughRestCashException;
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
    @Value("${custom.toss.secret}")
    private String SECRET_KEY;
    @Value("${custom.toss.client}")
    private String CLIENT_KEY;

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

        List<Order> orderList = orderService.findAllByBuyerId(authMember.getMember().getId());

        model.addAttribute("orders", orderList);

        return "order/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showOrderDetail(@PathVariable Long id, Model model,
                                  RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal AuthMember authMember) {

        Order newOrder = orderService.findByOrderId(id);
        long restCash = memberService.getRestCash(authMember.getMember());

        if (newOrder == null) {
            redirectAttributes.addFlashAttribute("result", "잘못된 주문 번호입니다.");
            return "redirect:/order/list";
        }

        model.addAttribute("order", newOrder);
        model.addAttribute("CLIENT_KEY", CLIENT_KEY);
        model.addAttribute("actorRestCash", restCash);

        return "order/detail";
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("isAuthenticated()")
    public String payByRestCashOnly(@AuthenticationPrincipal AuthMember authMember, @PathVariable long id) {
        Order order = orderService.findByOrderId(id);

        Member actor = authMember.getMember();

        if (!orderService.actorCanPayment(actor, order)) {
            throw new ActorCanNotPayOrderException();
        }

        orderService.payByRestCashOnly(order);

        return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("포인트로 결제했습니다."));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public String doOrderCancel(@PathVariable Long id, Model model,
                                  RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal AuthMember authMember) {

        Order findOrder = orderService.findByOrderId(id);

        if (findOrder == null) {
            redirectAttributes.addFlashAttribute("result", "잘못된 주문 번호입니다.");
            return "redirect:/order/list";
        }

        orderService.cancelOrder(findOrder);

        return "redirect:/order/list?msg=%s".formatted(Ut.url.encode("주문이 취소되었습니다."));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("isAuthenticated()")
    public String doOrderRefund(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal AuthMember authMember) {

        Order findOrder = orderService.findByOrderId(id);

        if (findOrder == null) {
            redirectAttributes.addFlashAttribute("result", "잘못된 주문 번호입니다.");
            return "redirect:/order/list";
        }

        if (orderService.isRefundable(findOrder)) {
            return "redirect:/order/%d?msg=%s".formatted(findOrder.getId(), Ut.url.encode("구매 후 10분이 지나 환불이 불가능합니다."));
        }

        orderService.refundOrder(findOrder);

        return "redirect:/order/list?msg=%s".formatted(Ut.url.encode("주문이 취소되었습니다."));
    }

    @RequestMapping("/{id}/success")
    public String confirmPayment(@PathVariable long id, @RequestParam String paymentKey,
                                 @RequestParam String orderId, @RequestParam Long amount, Model model,
                                 @AuthenticationPrincipal AuthMember authMember) throws Exception {

        Order order = orderService.findByOrderId(id);

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if (id != orderIdInputed) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = authMember.getMember();
        long restCash = memberService.getRestCash(actor);
        long payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new OrderNotEnoughRestCashException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("결제가 완료되었습니다."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@PathVariable long id, @RequestParam String message,
                              @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

}
