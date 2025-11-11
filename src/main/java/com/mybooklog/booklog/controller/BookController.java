package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.dto.BookCreateDto;
import com.mybooklog.booklog.dto.BookListResponseDto;
import com.mybooklog.booklog.dto.BookUpdateDto;
import com.mybooklog.booklog.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User; // Spring Security의 User
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 7주차: 책 목록 조회 (페이징 및 검색)
     */
    @GetMapping
    public String showBookList(Model model, @AuthenticationPrincipal User user,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @PageableDefault(size = 5, sort = "no", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BookListResponseDto> bookPage = bookService.findBooksByUser(user.getUsername(), keyword, pageable);

        // [수정됨] 템플릿과 일치하도록 변수 이름을 "bookPage"로 통일합니다.
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("keyword", keyword);
        return "book/list";
    }

    // 4주차: 책 등록 폼 (C)
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("form", new BookCreateDto());
        return "book/add";
    }

    // 4주차: 책 등록 처리 (C)
    @PostMapping
    public String addBook(@Valid @ModelAttribute("form") BookCreateDto form,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal User user,
                          RedirectAttributes rttr) {
        if (bindingResult.hasErrors()) {
            return "book/add";
        }
        Book savedBook = bookService.createBook(form, user.getUsername());
        rttr.addFlashAttribute("msg", "로컬 번호 #" + savedBook.getNo() + " 책이 등록되었습니다.");
        return "redirect:/books";
    }

    // 5~6주차: 책 상세 페이지 (R)
    @GetMapping("/{no}")
    public String showBookDetail(@PathVariable Long no, Model model, @AuthenticationPrincipal User user) {
        Book book = bookService.findBookByNoAndUser(no, user.getUsername());
        model.addAttribute("book", book);
        return "book/detail";
    }

    // 5~6주차: 책 수정 폼 페이지 (U)
    @GetMapping("/{no}/edit")
    public String editBookForm(@PathVariable Long no, Model model, @AuthenticationPrincipal User user) {
        Book book = bookService.findBookByNoAndUser(no, user.getUsername());

        BookUpdateDto form = new BookUpdateDto();
        form.setTitle(book.getTitle());
        form.setAuthor(book.getAuthor());

        model.addAttribute("form", form);
        model.addAttribute("no", book.getNo());
        return "book/edit";
    }

    // 5~6주차: 책 수정 처리 (U)
    @PostMapping("/{no}/edit")
    public String editBook(@PathVariable Long no,
                           @Valid @ModelAttribute("form") BookUpdateDto form,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal User user, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("no", no);
            return "book/edit";
        }

        bookService.updateBook(no, form, user.getUsername());
        return "redirect:/books/" + no;
    }

    // 5~6주차: 책 삭제 처리 (D)
    @PostMapping("/{no}/delete")
    public String deleteBook(@PathVariable Long no, @AuthenticationPrincipal User user, RedirectAttributes rttr) {
        bookService.deleteBook(no, user.getUsername());
        rttr.addFlashAttribute("msg", "로컬 번호 #" + no + " 책이 삭제되었습니다.");
        return "redirect:/books";
    }
}