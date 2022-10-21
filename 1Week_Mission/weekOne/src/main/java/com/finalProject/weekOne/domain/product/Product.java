package com.finalProject.weekOne.domain.product;

import com.finalProject.weekOne.domain.base.BaseEntity;
import com.finalProject.weekOne.domain.keyword.Keyword;
import com.finalProject.weekOne.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Product extends BaseEntity {

    private String subject;

    private int price;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Member author;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Keyword keyword;

    public Product(long id) {
        super(id);
    }

    public void modifyProduct(String subject, int price) {
        this.subject = subject;
        this.price = price;
    }
}
