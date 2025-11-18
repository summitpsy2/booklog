package com.mybooklog.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private int rating; // 별점 (1~5)

    // [수정됨] VARCHAR(500) 대신, 길이 제한이 없는 TEXT 타입으로 변경
    @Column(columnDefinition = "TEXT")
    private String comment; // 한줄평 (이제 긴 글 가능)

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 이 리뷰를 작성한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // 이 리뷰가 달린 책
}