package com.finalProject.mutbook.service.cartItem;

import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.product.Product;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartItemServiceTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

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
        productService.saveProduct(currentMember, dto);
    }

    @Test
    void addCartItem() {
        // given
        Member currentMember = memberService.findByUsername("ciTester");
        Product currentProduct = productService.findByProductId(1L);

        // when
        cartItemService.addItem(currentMember, currentProduct);

        // then
        assertThat(cartItemService.findAllByBuyer(currentMember).size()).isEqualTo(1);
//        assertThat(currentCartItem.getProducts().size()).isEqualTo(1);
    }
}