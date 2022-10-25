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
        dto.setSubject("SpringBoot");
        dto.setPrice(12000);
        Product product1 = productService.saveProduct(currentMember, dto);

        dto.setSubject("StringBoat");
        dto.setPrice(12000);
        Product product2 = productService.saveProduct(currentMember, dto);

        cartItemService.addItem(currentMember, product1);
        cartItemService.addItem(currentMember, product2);
    }

    @Test
    void createOrderFromCart() {
        // given
        Member currentMember = memberService.findByUsername("ciTester");
        System.out.println("currentMember.getUsername() = " + currentMember.getUsername());

        // when
        orderService.createOrder(currentMember);

        // then
        List<Order> orders = orderService.findAllByBuyer(currentMember.getId());
        assertThat(orders.size()).isEqualTo(1);
    }

}