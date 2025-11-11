package com.mybooklog.booklog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, message = "비밀번호는 6자 이상으로 입력해주세요.")
    private String password;
}