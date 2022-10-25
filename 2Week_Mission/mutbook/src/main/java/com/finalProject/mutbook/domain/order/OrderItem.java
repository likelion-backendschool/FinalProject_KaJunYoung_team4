package com.finalProject.mutbook.domain.order;

import com.finalProject.mutbook.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    private String name;

    private boolean isPaid; // 결제여부
    private boolean isCanceled; // 취소여부
    private boolean isRefunded; // 환불여부

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getSalePrice();
        }

        return payPrice;
    }

    public void setPaymentDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }

        isPaid = true;
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }

        isRefunded = true;
    }

    public int getPayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if (orderItems.size() > 1) {
            name += " 외 %d곡".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }

    public boolean isPayable() {
        if ( isPaid ) return false;
        if ( isCanceled ) return false;

        return true;
    }

}
