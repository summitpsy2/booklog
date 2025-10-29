package com.mybooklog.booklog.repository;

import com.mybooklog.booklog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // userId를 기준으로 모든 책을 찾아 반환하는 쿼리 메소드
    List<Book> findByUserId(Long userId);
}
