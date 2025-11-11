package com.mybooklog.booklog.repository;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 4주차에 추가한 메소드 (7주차에 페이징으로 변경 예정)
    List<Book> findByUserId(Long userId);

    // [ 6주차 추가 ]
    // 1. 특정 사용자의 '로컬 번호(no)'로 책 1권을 조회
    Optional<Book> findByUserAndNo(User user, Long no);

    // [ 6주차 추가 ]
    // 2. 새 책에 사용할 '로컬 번호'의 최대값을 조회
    @Query("SELECT MAX(b.no) FROM Book b WHERE b.user = :user")
    Long findMaxNoByUser(@Param("user") User user);

    // --- [ 7주차 수정/추가 ] ---

    // 1. [수정] 6주차의 'List<Book> findBooksByUser(User user)' 대신
    //    페이징과 정렬(no 기준 내림차순)이 적용된 메소드로 변경
    Page<Book> findByUserOrderByNoDesc(User user, Pageable pageable);

    // 2. [추가] '제목 검색' 기능이 포함된 페이징 메소드
    Page<Book> findByUserAndTitleContainingOrderByNoDesc(User user, String keyword, Pageable pageable);
}