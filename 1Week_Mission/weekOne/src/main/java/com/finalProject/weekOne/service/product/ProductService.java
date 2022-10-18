package com.finalProject.weekOne.service.product;

import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.post.Post;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.domain.product.ProductRepository;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * 새로운 Product를 저장하는 메소드
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

    /** id로 Product 객체를 찾는 메소드
     * @param id 찾으려는 product.id 번호
     */
    public Product findByProductId(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /** 모든 Product를 불러오는 메소드
     */
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }
}
