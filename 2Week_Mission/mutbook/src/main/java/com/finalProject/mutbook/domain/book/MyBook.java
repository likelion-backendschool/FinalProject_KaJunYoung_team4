package com.finalProject.mutbook.domain.book;

import com.finalProject.mutbook.domain.base.BaseEntity;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MyBook extends BaseEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    public MyBook(long id) {
        super(id);
    }
}
