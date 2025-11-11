package com.mybooklog.booklog.controller;

import com.mybooklog.booklog.dto.SignupRequestDto;
import com.mybooklog.booklog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // templates/auth/login.html
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequestDto());
        return "auth/signup"; // templates/auth/signup.html
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequestDto requestDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup"; // 유효성 검사 실패 시, 가입 폼으로 다시 이동
        }
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("username", "duplicate", e.getMessage());
            return "auth/signup"; // 아이디 중복 시, 가입 폼으로 다시 이동
        }
        return "redirect:/login?signupSuccess"; // 회원가입 성공 시 로그인 페이지로
    }
}