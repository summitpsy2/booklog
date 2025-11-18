package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.domain.Book;
import com.mybooklog.booklog.domain.Review;
import com.mybooklog.booklog.dto.BookCreateDto;
import com.mybooklog.booklog.dto.BookListResponseDto;
import com.mybooklog.booklog.dto.BookUpdateDto;
import com.mybooklog.booklog.dto.ReviewCreateDto;
import com.mybooklog.booklog.dto.ReviewUpdateDto;
// [수정] '나만의 서재' 컨셉에 불필요한 DTO import 삭제
// import com.mybooklog.booklog.dto.ReviewResponseDto;
// import com.mybooklog.booklog.dto.ReviewStatsDto;
import com.mybooklog.booklog.service.BookService;
import com.mybooklog.booklog.service.ReviewService;
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

// [수정] '나만의 서재' 컨셉에 불필요한 import 삭제
// import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ReviewService reviewService; // 8주차: ReviewService 주입

    /**
     * 2~7주차: '나의 책장' 목록 페이지 (R)
     */
    @GetMapping
    public String listBooks(@AuthenticationPrincipal User user,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                            Pageable pageable,
                            Model model) {

        String username = user.getUsername();

        Page<BookListResponseDto> bookPage = bookService.findBooksByUser(username, keyword, pageable);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("keyword", keyword);

        return "book/list";
    }

    /**
     * 4주차: 책 등록 폼 페이지 (C)
     */
    @GetMapping("/add")
    public String addBookForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new BookCreateDto());
        }
        return "book/add";
    }

    /**
     * 4주차 & 6주차: 책 등록 처리 (C)
     */
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

    /**
     * [수정됨] 8~9주차: '나만의 서재' 컨셉에 맞게 '내 리뷰' 정보만 전달 (+수정/삭제 폼 데이터 준비)
     */
    @GetMapping("/{no}")
    public String showBookDetail(@PathVariable Long no, Model model, @AuthenticationPrincipal User user) {

        String username = user.getUsername();

        // 1. 책 정보
        Book book = bookService.findBookByNoAndUser(no, username);

        // 2. 내 리뷰 조회 (있을 수도 있고, 없을 수도 있음)
        Optional<Review> myReviewOpt = reviewService.getMyReview(no, username);

        // 3. 공통 모델 값
        model.addAttribute("book", book);
        model.addAttribute("hasMyReview", myReviewOpt.isPresent());

        // 4. 내 리뷰가 있을 때: 수정 폼에 넣어줄 DTO & 리뷰 id
        if (myReviewOpt.isPresent()) {
            Review myReview = myReviewOpt.get();
            model.addAttribute("myReviewId", myReview.getId()); // 수정/삭제 URL에서 사용할 id

            // redirect 후 BindingResult 에러가 있을 때는 Flash Attribute 값 사용
            if (!model.containsAttribute("myReview")) {
                ReviewUpdateDto dto = new ReviewUpdateDto();
                dto.setRating(myReview.getRating());
                dto.setComment(myReview.getComment());
                model.addAttribute("myReview", dto);
            }
        }

        // 5. 새 리뷰 작성 폼 객체 (내 리뷰가 없을 때 사용)
        if (!model.containsAttribute("newReview")) {
            model.addAttribute("newReview", new ReviewCreateDto());
        }

        return "book/detail";
    }

    /**
     * 5~6주차: 책 수정 폼 페이지 (U)
     */
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

    /**
     * 5~6주차: 책 수정 처리 (U)
     */
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

    /**
     * 5~6주차: 책 삭제 처리 (D)
     */
    @PostMapping("/{no}/delete")
    public String deleteBook(@PathVariable Long no, @AuthenticationPrincipal User user, RedirectAttributes rttr) {
        bookService.deleteBook(no, user.getUsername());
        rttr.addFlashAttribute("msg", "로컬 번호 #" + no + " 책이 삭제되었습니다.");
        return "redirect:/books";
    }
}