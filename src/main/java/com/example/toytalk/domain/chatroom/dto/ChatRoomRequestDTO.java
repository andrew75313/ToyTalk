package com.example.toytalk.domain.chatroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatRoomRequestDTO {

    @NotBlank(message = "대화방 제목은 필수 입력해주세요.")
    private String title;

    @NotBlank(message = "대화방 카테고리는 필수 입력해주세요.")
    private String category;

    private Boolean isPrivate;

    private String password;
}
