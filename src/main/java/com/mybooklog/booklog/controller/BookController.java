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

import org.springframework.web.bind.annotation.PathVariable; // 5주차 추가
import java.util.Optional; // 5주차 추가

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

    @GetMapping("/books/{id}")
    public String showBookDetail(@PathVariable Long id, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if(optionalBook.isPresent()) {
            model.addAttribute("book", optionalBook.get());
            return "book-detail";
        } else {
            return "redirect:/books";
        }
    }

    @GetMapping("/books/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            model.addAttribute("book", optionalBook.get());
            return "edit-book-form";
        } else {
            return "redirect:/books";
        }
    }

    @PostMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, @ModelAttribute Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
        return "redirect:/books/{id}";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}

