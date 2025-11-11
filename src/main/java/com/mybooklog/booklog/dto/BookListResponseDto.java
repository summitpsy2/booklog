package com.mybooklog.booklog.dto;

import com.mybooklog.booklog.domain.Book;
import lombok.Getter;

@Getter
public class BookListResponseDto {

    private final Long no;
    private final String title;
    private final String author;

    // Book 엔티티를 이 DTO로 변환하는 생성자
    public BookListResponseDto(Book book) {
        this.no = book.getNo();
        this.title = book.getTitle();
        this.author = book.getAuthor();
    }
}