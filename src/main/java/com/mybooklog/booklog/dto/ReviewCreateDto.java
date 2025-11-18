package com.mybooklog.booklog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {

    @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5점 이하이어야 합니다.")
    private int rating = 5; // 기본값 5점

    @NotBlank(message = "코멘트를 입력해주세요.")
    private String comment; // Markdown 텍스트
}