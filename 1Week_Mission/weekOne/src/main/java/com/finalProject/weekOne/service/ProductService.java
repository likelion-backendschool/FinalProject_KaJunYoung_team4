package com.finalProject.weekOne.service;

import com.finalProject.weekOne.domain.app.util.Ut;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.product.Product;
import com.finalProject.weekOne.domain.product.ProductRepository;
import com.finalProject.weekOne.web.dto.product.CreateProductDto;
import com.finalProject.weekOne.web.dto.product.ModifyProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** Product를 삭제하는 메소드
     * @param currentProduct id를 통해 찾은 Product
     */
    public void removeProduct(Product currentProduct) {
        productRepository.delete(currentProduct);
    }

    /**
     * Product를 수정하는 메소드
     *
     * @param currentProduct 수정하기 전 Product
     * @param modifyProductDto Product의 subject, price를 가지고 있는 DTO
     */
    @Transactional
    public void modifyProduct(Product currentProduct, ModifyProductDto modifyProductDto) {
        currentProduct.modifyProduct(modifyProductDto.getSubject(),
                modifyProductDto.getPrice());
    }
}
