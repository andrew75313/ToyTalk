package com.example.toytalk.domain.chatroom.repository;


import com.example.toytalk.domain.chatroom.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {

    @Query("SELECT c FROM Chatroom c WHERE c.status = 'ACTIVATED' ORDER BY c.createdAt DESC")
    Page<Chatroom> findAllActivated(Pageable pageable);

    @Query("SELECT c FROM Chatroom c WHERE c.status = 'ACTIVATED' AND c.id = :chatroomId")
    Optional<Chatroom> findActivatedRoomById(UUID chatroomId);
}
