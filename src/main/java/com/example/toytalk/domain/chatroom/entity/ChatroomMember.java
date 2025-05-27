package com.example.toytalk.domain.chatroom.entity;

import com.example.toytalk.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chatroom_members")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomMember {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID chatroomId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    @Column(nullable = false)
    private Boolean isJoined;

    public void updateIsJoined(Boolean isJoined) {
        this.isJoined = isJoined;
    }

    public void updateLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }
}
