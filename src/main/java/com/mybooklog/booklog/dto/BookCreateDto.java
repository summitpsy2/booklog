package com.mybooklog.booklog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateDto {

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String title;

    @NotBlank(message = "저자는 비워둘 수 없습니다.")
    private String author;
}