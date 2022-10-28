package com.finalProject.mutbook.service.order;

import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.domain.product.Product;
import com.finalProject.mutbook.service.cartItem.CartItemService;
import com.finalProject.mutbook.service.member.MemberService;
import com.finalProject.mutbook.service.product.ProductService;
import com.finalProject.mutbook.web.dto.member.SignUpDto;
import com.finalProject.mutbook.web.dto.product.CreateProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void testBefore() {
        String username = "ciTester";
        String password = "ciTester1234!";
        String email = "ciTester@test.com";

        SignUpDto dto = new SignUpDto();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setEmail(email);

        memberService.join(dto);
        createProduct();
    }

    void createProduct() {
        Member currentMember = memberService.findByUsername("ciTester");
        CreateProductDto dto = new CreateProductDto();

        for (int i = 0; i < 100; i++) {
            dto.setSubject("SpringBoot");
            dto.setPrice(12000);
            Product product = productService.saveProduct(currentMember, dto);
            cartItemService.addItem(currentMember, product);
        }
    }

    @Test
    void createOrderFromCart() {
        // given
        Member currentMember = memberService.findByUsername("ciTester");
        System.out.println("currentMember.getUsername() = " + currentMember.getUsername());

        // when
        orderService.createOrder(currentMember);

        // then
        List<Order> orders = orderService.findAllByBuyerId(currentMember.getId());
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("findByMemberId")
    void findByMemberId() {
        // given
        Member currentMember = memberService.findByUsername("ciTester");
        System.out.println("--------------------------------");

        // when
        orderService.createOrder(currentMember);

        // then
        List<Order> orders = orderService.findAllByBuyerId(currentMember.getId());
        System.out.println("=================================");
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("findByMember")
    void findByMember() {
        // given
        Member currentMember = memberService.findByUsername("ciTester");
        System.out.println("--------------------------------");

        // when
        orderService.createOrder(currentMember);

        // then
        List<Order> orders = orderService.findAllByBuyer(currentMember);
        System.out.println("=================================");
        assertThat(orders.size()).isEqualTo(1);
    }

}