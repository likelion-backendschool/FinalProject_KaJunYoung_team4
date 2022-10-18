package com.finalProject.weekOne.domain.keyword;

import com.finalProject.weekOne.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Keyword extends BaseEntity {
    private String content;

    public Keyword(long id) {
        super(id);
    }

    public String getListUrl() {
        return "/post/list?kwType=keyword&kw=%s".formatted(content);
    }
}
