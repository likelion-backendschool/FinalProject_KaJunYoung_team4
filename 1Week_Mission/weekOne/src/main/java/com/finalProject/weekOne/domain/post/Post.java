package com.finalProject.weekOne.domain.post;

import com.finalProject.weekOne.domain.base.BaseEntity;
import com.finalProject.weekOne.domain.member.Member;
import com.finalProject.weekOne.domain.tag.HashTag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

    public Post(long id) {
        super(id);
    }

    public void modifyPost(String subject, String content, String contentHtml) {
        this.subject = subject;
        this.content = content;
        this.contentHtml = contentHtml;
    }

    public String getExtra_inputValue_hashTagContents() {
        Map<String, Object> extra = getExtra();

        if (extra.containsKey("hashTags") == false) {
            return "";
        }

        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");

        if (hashTags.isEmpty()) {
            return "";
        }

        return hashTags
                .stream()
                .map(hashTag -> "#" + hashTag.getKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getExtra_hashTagLinks() {
        Map<String, Object> extra = getExtra();

        if (!extra.containsKey("hashTags")) {
            return "";
        }

        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");

        if (hashTags.isEmpty()) {
            return "";
        }

        return hashTags
                .stream()
                .map(hashTag -> {
                    String text = "#" + hashTag.getKeyword().getContent();

                    return """
                            <a href="%s" target="_blank">%s</a>
                            """
                            .stripIndent()
                            .formatted(hashTag.getKeyword().getListUrl(), text);
                })
                .sorted()
                .collect(Collectors.joining(" "));
    }

}
