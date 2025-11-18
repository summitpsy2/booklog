package com.mybooklog.booklog.service;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.Review;
import com.mybooklog.booklog.domain.User;
import com.mybooklog.booklog.dto.ReviewCreateDto;
import com.mybooklog.booklog.dto.ReviewUpdateDto; // [9주차] import
import com.mybooklog.booklog.repository.ReviewRepository;
import com.mybooklog.booklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    // [C] 리뷰 생성 (8주차)
    @Transactional
    public Review createReview(ReviewCreateDto dto, Long bookNo, String username) {
        User user = findUserByUsername(username);
        Book book = bookService.findBookByNoAndUser(bookNo, username);

        if (reviewRepository.findByBookAndUser(book, user).isPresent()) {
            throw new IllegalArgumentException("이미 이 책에 대한 리뷰를 작성했습니다.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return reviewRepository.save(review);
    }

    // [R] '나의' 리뷰 조회 (8주차)
    public Optional<Review> getMyReview(Long bookNo, String username) {
        User user = findUserByUsername(username);
        Book book = bookService.findBookByNoAndUser(bookNo, username);
        return reviewRepository.findByBookAndUser(book, user);
    }

    // --- [ 9주차 추가: 내 리뷰 수정 ] ---
    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateDto dto, String username) {
        User user = findUserByUsername(username);
        // [핵심] 9주차에 Repository에 추가한 메소드로 '내 리뷰'가 맞는지 조회
        Review review = reviewRepository.findByIdAndUser(reviewId, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없거나 수정 권한이 없습니다."));

        // 내용 수정 (JPA의 Dirty Checking으로 자동 저장됨)
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
    }

    // --- [ 9주차 추가: 내 리뷰 삭제 ] ---
    @Transactional
    public void deleteReview(Long reviewId, String username) {
        User user = findUserByUsername(username);
        // [핵심] '내 리뷰'가 맞는지 조회
        Review review = reviewRepository.findByIdAndUser(reviewId, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없거나 삭제 권한이 없습니다."));

        // 리뷰 삭제
        reviewRepository.delete(review);
    }

    // (Helper) 중복 코드를 줄이기 위한 사용자 조회 메소드 (8주차)
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}