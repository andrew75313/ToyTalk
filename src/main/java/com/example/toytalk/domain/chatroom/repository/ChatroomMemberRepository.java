package com.example.toytalk.domain.chatroom.repository;

import com.example.toytalk.domain.chatroom.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, UUID> {
    List<ChatroomMember> findAllByChatroomId(UUID chatroomId);

    @Query("SELECT COUNT(c) > 0 FROM ChatroomMember c WHERE c.chatroomId = :chatroomId AND c.userId = :userId")
    Boolean isUserInChatroom(UUID userId, UUID chatroomId);

    @Query("SELECT c FROM ChatroomMember c WHERE c.chatroomId = :chatroomId AND c.userId = :userId AND c.isJoined = TRUE")
    Optional<ChatroomMember> findJoinedMemberInChatroom(UUID chatroomId, UUID id);
}
