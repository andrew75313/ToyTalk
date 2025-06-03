package com.example.toytalk.domain.chatroom.dto;

import com.example.toytalk.domain.chatroom.entity.Chatroom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatroomResponseDTO {
    private String id;
    private String title;
    private String category;
    private Long memberCount;
    private Boolean isPrivate;
    private String createdBy;
    private LocalDateTime createdAt;

    public ChatroomResponseDTO(Chatroom chatroom) {
        this.id = chatroom.getId().toString();
        this.title = chatroom.getTitle();
        this.category = chatroom.getCategory();
        this.isPrivate = chatroom.getIsPrivate();
        this.createdBy = chatroom.getCreatedBy().toString();
        this.createdAt = chatroom.getCreatedAt();
    }
}
