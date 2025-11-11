package com.mybooklog.booklog.service;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.User;
import com.mybooklog.booklog.dto.BookCreateDto;
import com.mybooklog.booklog.dto.BookUpdateDto;
import com.mybooklog.booklog.repository.BookRepository;
import com.mybooklog.booklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.mybooklog.booklog.dto.BookListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * 책 생성 + [6주차 로컬 번호(no) 생성 로직 추가]
     */
    @Transactional
    public Book createBook(BookCreateDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 1. 현재 사용자의 책 중 가장 큰 'no' 값을 조회
        Long maxNo = bookRepository.findMaxNoByUser(user);
        long nextNo = (maxNo == null) ? 1 : maxNo + 1; // 없으면 1, 있으면 +1

        Book newBook = new Book();
        newBook.setTitle(dto.getTitle());
        newBook.setAuthor(dto.getAuthor());
        newBook.setUser(user);
        newBook.setNo(nextNo); // 2. 계산된 로컬 번호(no)를 설정

        return bookRepository.save(newBook);
    }

    /**
     * 내 책 목록 조회
     */
    /**
     * [수정됨] 7주차: 내 책 목록 조회 (페이징 및 검색 기능 추가)
     */
    public Page<BookListResponseDto> findBooksByUser(String username, String keyword, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Book> bookPage;
        if (keyword != null && !keyword.isEmpty()) {
            // 1. 검색어가 있으면: '제목'으로 검색 + 페이징
            bookPage = bookRepository.findByUserAndTitleContainingOrderByNoDesc(user, keyword, pageable);
        } else {
            // 2. 검색어가 없으면: 전체 목록 + 페이징
            bookPage = bookRepository.findByUserOrderByNoDesc(user, pageable);
        }

        // 3. Page<Book>을 Page<BookListResponseDto>로 변환하여 반환
        return bookPage.map(BookListResponseDto::new);
    }

    /**
     * [수정됨] 책 상세 조회 (id -> no)
     */
    public Book findBookByNoAndUser(Long no, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return bookRepository.findByUserAndNo(user, no)
                .orElseThrow(() -> new IllegalArgumentException("해당 책을 찾을 수 없습니다. (번호: " + no + ")"));
    }

    /**
     * [수정됨] 책 정보 수정 (id -> no)
     */
    @Transactional
    public void updateBook(Long no, BookUpdateDto dto, String username) {
        // 조회 시 권한 검사 자동 완료
        Book originalBook = findBookByNoAndUser(no, username);

        originalBook.setTitle(dto.getTitle());
        originalBook.setAuthor(dto.getAuthor());
        // @Transactional에 의해 자동 저장 (Dirty Checking)
    }

    /**
     * [수정됨] 책 삭제 (id -> no)
     */
    @Transactional
    public void deleteBook(Long no, String username) {
        // 조회 시 권한 검사 자동 완료
        Book book = findBookByNoAndUser(no, username);
        bookRepository.delete(book);
    }
}