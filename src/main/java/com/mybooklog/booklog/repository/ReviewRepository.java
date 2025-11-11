package com.mybooklog.booklog.repository;
import com.mybooklog.booklog.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}