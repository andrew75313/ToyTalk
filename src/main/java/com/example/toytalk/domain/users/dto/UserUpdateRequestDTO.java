package com.example.toytalk.domain.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserUpdateRequestDTO {

    @NotNull(message = "내용이 없다면, 빈칸으로 유지해주세요")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String newEmail;

    @NotNull(message = "내용이 없다면, 빈칸으로 유지해주세요")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "휴대전화번호는 010-1234-5678 형식으로 입력해주세요.")
    private String newPhoneNumber;
}
