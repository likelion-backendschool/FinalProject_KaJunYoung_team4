package com.finalProject.mutbook.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    List<MyBook> findAllByOrderId(long orderId);

    boolean existsByMemberIdAndProductId(long memberId, long productId);
}
