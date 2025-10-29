package com.mybooklog.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String title;

    @Column(nullable = false)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위해 LAZY 옵션 추가
    @JoinColumn(name = "user_id") // DB에 생성될 외래키(FK) 컬럼의 이름을 'user_id'로 지정
    private User user;
}
