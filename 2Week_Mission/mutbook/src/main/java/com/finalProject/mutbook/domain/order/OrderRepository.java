package com.finalProject.mutbook.domain.order;

import com.finalProject.mutbook.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyerId(Long id);

    List<Order> findAllByBuyer(Member buyer);
}
