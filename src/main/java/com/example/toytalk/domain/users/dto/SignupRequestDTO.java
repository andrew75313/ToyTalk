package com.example.toytalk.domain.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDTO {

    @NotBlank(message = "아이디는 필수 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[0-9]).{8,16}$",
            message = "비밀번호는 대문자,특수문자,숫자 각각 1개 이상 포함한 8~16자리 이어야합니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대전화번호는 필수 입력해주세요.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "휴대전화번호는 010-1234-5678 형식으로 입력해주세요.")
    private String phoneNumber;
}
