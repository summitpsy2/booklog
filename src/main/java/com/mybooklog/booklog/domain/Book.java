package com.mybooklog.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp; // import
import java.time.LocalDateTime; // import

@Entity
@Getter @Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") // ID 컬럼 이름을 명시
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "user_book_number")
    private Long no; // 사용자별 로컬 번호 (1, 2, 3...)

    @CreationTimestamp // 엔티티가 처음 저장될 때 자동으로 시간 저장
    private LocalDateTime createdAt;

    // --- 관계 설정 ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 이 책의 주인 (User 엔티티 참조)
}