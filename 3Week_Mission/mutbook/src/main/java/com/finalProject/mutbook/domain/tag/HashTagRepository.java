package com.finalProject.mutbook.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findByPostIdAndKeywordId(Long postId, Long keywordId);

    List<HashTag> findAllByPostId(Long id);

    List<HashTag> findAllByPostIdIn(long[] ids);
}
