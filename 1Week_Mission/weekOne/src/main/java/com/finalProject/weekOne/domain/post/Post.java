package com.finalProject.weekOne.domain.post;

import com.finalProject.weekOne.domain.base.BaseEntity;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.tag.HashTag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String subject;
    private String content;
    private String contentHtml;

    @ManyToOne(fetch = LAZY)
    private Member author;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private List<HashTag> hashTags = new ArrayList<>();

    public Post(long id) {
        super(id);
    }

    public void modifyPost(String subject, String content, String contentHtml) {
        this.subject = subject;
        this.content = content;
        this.contentHtml = contentHtml;
    }

    // 해당 게시글의 해시태그들을 한 문장으로 반환
    public String getHashTagString() {
        if(hashTags.isEmpty()) {
            return "";
        }

        return "#" + hashTags
                .stream()
                .map(hashTag -> hashTag.getKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" #"))
                .trim();
    }

}
