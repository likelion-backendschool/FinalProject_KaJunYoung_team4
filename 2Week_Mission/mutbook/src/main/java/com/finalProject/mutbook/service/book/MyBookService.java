package com.finalProject.mutbook.service.book;

import com.finalProject.mutbook.domain.book.MyBook;
import com.finalProject.mutbook.domain.book.MyBookRepository;
import com.finalProject.mutbook.domain.member.Member;
import com.finalProject.mutbook.domain.order.Order;
import com.finalProject.mutbook.domain.order.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyBookService {

    private final MyBookRepository myBookRepository;

    public void saveBook(Order order) {
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
            MyBook myBook = MyBook.builder()
                    .member(order.getBuyer())
                    .product(item.getProduct())
                    .build();
            myBookRepository.save(myBook);
        }
    }

    public void deleteBook(Order order) {
        List<MyBook> myBooks = findAllByOrderId(order.getId());
        myBookRepository.deleteAll(myBooks);
    }

    public List<MyBook> findAllByOrderId(long orderId) {
        return myBookRepository.findAllByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public boolean existProduct(long memberId, long productId) {
        return myBookRepository.existsByMemberIdAndProductId(memberId, productId);
    }
}
