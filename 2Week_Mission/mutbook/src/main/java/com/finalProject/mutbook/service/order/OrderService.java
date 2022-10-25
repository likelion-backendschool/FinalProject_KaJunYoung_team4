package com.finalProject.mutbook.service.order;

import com.finalProject.mutbook.domain.cartItem.CartItem;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.domain.order.OrderItem;
import com.finalProject.mutbook.domain.order.OrderRepository;
import com.finalProject.mutbook.domain.product.Product;
import com.finalProject.mutbook.service.cartItem.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Member currentMember) {
        List<CartItem> cartItems = cartItemService.findAllByBuyer(currentMember);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product currentProduct = cartItem.getProduct();
            orderItems.add(new OrderItem(currentProduct));
        }

        return create(currentMember, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .buyer(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        // 주문 품목으로 부터 이름을 만든다.
        order.makeName();

        orderRepository.save(order);

        return order;
    }

    public List<Order> findAllByBuyer(Long buyerId) {
        return orderRepository.findAllByBuyerId(buyerId);
    }
}
