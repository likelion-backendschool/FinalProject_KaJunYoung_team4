package com.finalProject.mutbook.service.tag;

import com.finalProject.mutbook.domain.keyword.Keyword;
import com.finalProject.mutbook.domain.post.Post;
import com.finalProject.mutbook.domain.tag.HashTag;
import com.finalProject.mutbook.domain.tag.HashTagRepository;
import com.finalProject.mutbook.service.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    private final KeywordService keywordService;

    public void applyHashTags(Post post, String hashTagContents) {
        List<HashTag> oldHashTags = getHashTags(post);

        List<String> keywordContents = Arrays.stream(hashTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0).toList();

        List<HashTag> needToDelete = new ArrayList<>();

        for (HashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));

            if (!contains) {
                needToDelete.add(oldHashTag);
            }
        }

        needToDelete.forEach(hashTag -> {
            hashTagRepository.delete(hashTag);
        });

        keywordContents.forEach(keywordContent -> {
            saveHashTag(post, keywordContent);
        });
    }

    private HashTag saveHashTag(Post post, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .post(post)
                .author(post.getAuthor())
                .createDate(LocalDateTime.now())
                .keyword(keyword)
                .build();

        hashTagRepository.save(hashTag);

        return hashTag;
    }

    public List<HashTag> getHashTags(Post post) {
        return hashTagRepository.findAllByPostId(post.getId());
    }

    public List<HashTag> getHashTagsByArticleIdIn(long[] ids) {
        return hashTagRepository.findAllByPostIdIn(ids);
    }
}
