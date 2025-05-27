package com.example.toytalk.domain.chatroom.entity;

import com.example.toytalk.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "chatrooms")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom extends Timestamped {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatroomStatus status;

    @Column(nullable = false)
    private Boolean isPrivate;

    private String password;

    @Column(nullable = false)
    private UUID createdBy;

    public void updateStatus(ChatroomStatus status) {
        this.status = status;
    }
}