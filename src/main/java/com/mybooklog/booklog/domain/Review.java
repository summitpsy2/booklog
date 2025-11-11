package com.mybooklog.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp; // import
import java.time.LocalDateTime; // import

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

    @Column(length = 500)
    private String comment; // 한줄평

    @UpdateTimestamp // 엔티티가 수정될 때마다 자동으로 시간 갱신
    private LocalDateTime updatedAt;

    // --- 관계 설정 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 이 리뷰를 작성한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // 이 리뷰가 달린 책
}