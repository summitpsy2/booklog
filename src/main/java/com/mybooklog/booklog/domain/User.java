package com.mybooklog.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList; // import
import java.util.List; // import

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    // --- [이 부분을 추가합니다] ---
    // 한 명의 유저는 여러 개의 책을 가질 수 있다.
    // "내가 주인이 아닌" Book 엔티티의 'user' 필드에 의해 매핑됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
    // -------------------------
}
    
