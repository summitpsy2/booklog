package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/books/add")
    public String showAddBookForm() {
        return "add-book-form";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String showBookList(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "book-list";
    }
}

