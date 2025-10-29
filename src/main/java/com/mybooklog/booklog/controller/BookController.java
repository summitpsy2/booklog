package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.User;
import com.mybooklog.booklog.repository.BookRepository;
import com.mybooklog.booklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository; // 사용자 정보를 찾기 위해 주입

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book-form";
    }

    // 2. 책 등록 처리 (6주차: 사용자 정보 연동)
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        book.setUser(user); // 책의 주인(User)을 설정
        bookRepository.save(book);
        return "redirect:/books";
    }

    // 3. 책 목록 조회 (6주차: '내 책'만 조회)
    @GetMapping("/books")
    public String listBooks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Book> books = bookRepository.findByUserId(user.getId());
        model.addAttribute("books", books);
        return "book-list";
    }

    // 4. 책 상세 페이지 (6주차: 소유권 검사)
    @GetMapping("/books/{id}")
    public String showBookDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));

        // [보안 로직] 책의 주인이 없거나(null) 현재 로그인한 사용자와 다르면, 목록으로 튕겨냄
        if (userDetails == null || book.getUser() == null || !book.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "book-detail";
    }

    // 5. 책 수정 폼 페이지 (6주차: 소유권 검사)
    @GetMapping("/books/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));

        // [보안 로직]
        if (userDetails == null || book.getUser() == null || !book.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/books";
        }
        model.addAttribute("book", book);
        return "edit-book-form";
    }

    // 6. 책 수정 처리 (6주차: 소유권 검사 및 '책 사라짐 버그' 수정)
    @PostMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, @ModelAttribute Book formBook, @AuthenticationPrincipal UserDetails userDetails) {

        // 1. DB에서 원래 책 정보를 찾아옵니다.
        Book originalBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        // 2. [보안 로직] 이 책의 주인이 현재 로그인한 사용자인지 확인합니다.
        if (userDetails == null || originalBook.getUser() == null || !originalBook.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/books";
        }

        // 3. '책 사라짐 버그' 해결: 폼에서 받은 내용(제목, 저자)으로 '원본' 객체를 수정
        originalBook.setTitle(formBook.getTitle());
        originalBook.setAuthor(formBook.getAuthor());

        // 4. '주인' 정보가 그대로 유지된 원본 객체를 저장합니다.
        bookRepository.save(originalBook);

        return "redirect:/books/{id}";
    }

    // 7. 책 삭제 처리 (6주차: 소유권 검사)
    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        // [보안 로직]
        if (userDetails == null || book.getUser() == null || !book.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/books";
        }

        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}