package com.finalProject.weekOne.service.product;

import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.domain.product.ProductRepository;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * 새로운 게시물을 저장하는 메소드
     *
     * @param member        현재 로그인된 Member
     * @param createProductDto subject, price, postKeywordId가 포함된 DTO
     */
    public Product saveProduct(Member member, CreateProductDto createProductDto) {

        Product newProduct = Product.builder()
                .subject(createProductDto.getSubject())
                .price(createProductDto.getPrice())
                .author(member)
                .createDate(LocalDateTime.now())
                .build();

        productRepository.save(newProduct);

        return newProduct;
    }
}
