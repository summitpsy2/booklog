package com.mybooklog.booklog.repository;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.Review;
import com.mybooklog.booklog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ... (8주차에 추가한 메소드들) ...
    List<Review> findByBookOrderByUpdatedAtDesc(Book book);
    Optional<Review> findByBookAndUser(Book book, User user);
    @Query("SELECT r.rating FROM Review r WHERE r.book = :book")
    List<Integer> findAllRatingsByBook(@Param("book") Book book);

    // --- [ 9주차 추가 ] ---
    // 리뷰 ID(id)와 작성자(user)를 조합하여, '내 리뷰'가 맞는지 확인
    Optional<Review> findByIdAndUser(Long id, User user);
}