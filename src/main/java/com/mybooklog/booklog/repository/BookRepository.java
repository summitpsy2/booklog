package com.mybooklog.booklog.repository;

import com.mybooklog.booklog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
