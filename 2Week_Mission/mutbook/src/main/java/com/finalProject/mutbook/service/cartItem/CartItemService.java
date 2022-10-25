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

    public List<CartItem> findAllByBuyer(Member buyer) {
        return cartItemRepository.findAllByBuyerId(buyer.getId());
    }
}
