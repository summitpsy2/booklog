package com.mybooklog.booklog.service;

import com.mybooklog.booklog.domain.User;
import com.mybooklog.booklog.dto.SignupRequestDto;
import com.mybooklog.booklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(SignupRequestDto requestDto) {
        // 1. 아이디 중복 검사
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 2. DTO -> Entity 변환 및 비밀번호 암호화
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        return userRepository.save(user);
    }
}