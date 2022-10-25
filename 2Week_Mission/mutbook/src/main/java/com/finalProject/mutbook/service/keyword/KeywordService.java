package com.finalProject.mutbook.service.keyword;

import com.finalProject.mutbook.domain.keyword.Keyword;
import com.finalProject.mutbook.domain.keyword.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword save(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);

        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }

        Keyword keyword = Keyword
                .builder()
                .content(keywordContent)
                .createDate(LocalDateTime.now())
                .build();

        keywordRepository.save(keyword);

        return keyword;
    }
}
