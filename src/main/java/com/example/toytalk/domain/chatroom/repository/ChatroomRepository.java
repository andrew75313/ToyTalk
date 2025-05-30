package com.example.toytalk.domain.chatroom.repository;

import com.example.toytalk.domain.chatroom.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {

    @Query("SELECT c FROM Chatroom c WHERE c.status = 'ACTIVATED' ORDER BY c.createdAt DESC")
    List<Chatroom> findAllActivated();

    @Query("SELECT c FROM Chatroom c WHERE c.status = 'ACTIVATED' AND c.id = :chatroomId")
    Optional<Chatroom> findActivatedRoomById(UUID chatroomId);
}
