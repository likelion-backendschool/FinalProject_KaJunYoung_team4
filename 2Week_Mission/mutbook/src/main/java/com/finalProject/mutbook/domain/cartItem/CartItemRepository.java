package com.finalProject.mutbook.domain.cartItem;

import com.finalProject.mutbook.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByBuyerId(Long buyerId);

    Optional<CartItem> findByBuyerIdAndProductId(Long buyerId, Long productId);

    void deleteByBuyerIdAndProductId(Long memberId, Long productId);
}
