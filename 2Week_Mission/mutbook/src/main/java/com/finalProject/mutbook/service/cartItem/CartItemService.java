package com.finalProject.mutbook.service.cartItem;

import com.finalProject.mutbook.domain.cartItem.CartItem;
import com.finalProject.mutbook.domain.cartItem.CartItemRepository;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    /** 장바구니에 Product 추가하는 메소드
     * @param buyer 현재 로그인된 Member
     * @param product 선택된 상품
     */
    @Transactional
    public CartItem addItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);

        if (oldCartItem != null) {
            return oldCartItem;
        }

        CartItem cartItem = CartItem.builder()
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    /** 장바구니에 Product 삭제하는 메소드
     * @param buyerId 현재 로그인된 memberId
     * @param productId 선택된 상품의 productId
     */
    @Transactional
    public void removeItem(Long buyerId, Long productId) {
        CartItem currentItem = cartItemRepository.findByBuyerIdAndProductId(buyerId, productId).orElse(null);
        if (currentItem != null) {
            cartItemRepository.delete(currentItem);
        }
    }

    /** 로그인된 Member 장바구니를 보여주는 메소드
     * @param buyer 현재 로그인된 Member
     */
    @Transactional(readOnly = true)
    public List<CartItem> findAllByBuyer(Member buyer) {
        return cartItemRepository.findAllByBuyerId(buyer.getId());
    }

    @Transactional(readOnly = true)
    public boolean existProduct(long memberId, long productId) {
        return cartItemRepository.existsByBuyerIdAndProductId(memberId, productId);
    }
}
