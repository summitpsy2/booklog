package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.dto.ReviewCreateDto;
import com.mybooklog.booklog.dto.ReviewUpdateDto;
import com.mybooklog.booklog.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // [C] 리뷰 생성 (8주차)
    @PostMapping("/books/{no}/reviews")
    public String createReview(@PathVariable Long no,
                               @Valid @ModelAttribute("newReview") ReviewCreateDto dto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes rttr) {

        if (bindingResult.hasErrors()) {
            rttr.addFlashAttribute("org.springframework.validation.BindingResult.newReview", bindingResult);
            rttr.addFlashAttribute("newReview", dto);
            return "redirect:/books/" + no;
        }

        try {
            reviewService.createReview(dto, no, user.getUsername());
            rttr.addFlashAttribute("msg", "리뷰가 성공적으로 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            rttr.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/books/" + no;
    }

    // [U] 내 리뷰 수정 (9주차)
    @PostMapping("/reviews/{reviewId}/edit")
    public String updateReview(@PathVariable Long reviewId,
                               @RequestParam Long bookNo,
                               @Valid @ModelAttribute("myReview") ReviewUpdateDto dto,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes rttr) {

        if (bindingResult.hasErrors()) {
            rttr.addFlashAttribute("org.springframework.validation.BindingResult.myReview", bindingResult);
            rttr.addFlashAttribute("myReview", dto);
            return "redirect:/books/" + bookNo;
        }

        try {
            reviewService.updateReview(reviewId, dto, user.getUsername());
            rttr.addFlashAttribute("msg", "리뷰가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            rttr.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/books/" + bookNo;
    }

    // [D] 내 리뷰 삭제 (9주차)
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId,
                               @RequestParam Long bookNo,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes rttr) {

        try {
            reviewService.deleteReview(reviewId, user.getUsername());
            rttr.addFlashAttribute("msg", "리뷰가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            rttr.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/books/" + bookNo;
    }
}