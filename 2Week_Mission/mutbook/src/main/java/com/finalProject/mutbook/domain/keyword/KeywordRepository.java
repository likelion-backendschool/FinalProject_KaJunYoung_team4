package com.finalProject.mutbook.domain.keyword;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByContent(String keywordContent);
}
