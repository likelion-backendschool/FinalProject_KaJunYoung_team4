package com.finalProject.mutbook.domain.tag;

import com.finalProject.mutbook.domain.base.BaseEntity;
import com.finalProject.mutbook.domain.keyword.Keyword;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.post.Post;
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
public class HashTag extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Member author;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Post post;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Keyword keyword;

    public HashTag(long id) {
        super(id);
    }
}
