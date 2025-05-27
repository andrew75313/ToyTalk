package com.example.toytalk.domain.chatroom.repository;

import com.example.toytalk.domain.chatroom.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, UUID> {
    List<ChatroomMember> findAllByChatroomId(UUID chatroomId);
}
