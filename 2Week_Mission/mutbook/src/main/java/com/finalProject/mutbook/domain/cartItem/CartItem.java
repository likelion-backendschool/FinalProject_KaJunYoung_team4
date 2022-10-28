package com.finalProject.mutbook.domain.cartItem;

import com.finalProject.mutbook.domain.base.BaseEntity;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class CartItem extends BaseEntity {

    @ManyToOne
    private Product product;

    @ManyToOne
    private Member buyer;

    public CartItem(long id) {
        super(id);
    }

}
