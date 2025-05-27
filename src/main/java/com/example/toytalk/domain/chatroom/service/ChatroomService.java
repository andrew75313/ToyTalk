package com.example.toytalk.domain.chatroom.service;

import com.example.toytalk.domain.chatroom.dto.ChatRoomRequestDTO;
import com.example.toytalk.domain.chatroom.dto.ChatroomResponseDTO;
import com.example.toytalk.domain.chatroom.entity.Chatroom;
import com.example.toytalk.domain.chatroom.entity.ChatroomMember;
import com.example.toytalk.domain.chatroom.entity.ChatroomStatus;
import com.example.toytalk.domain.chatroom.repository.ChatroomMemberRepository;
import com.example.toytalk.domain.chatroom.repository.ChatroomRepository;
import com.example.toytalk.domain.users.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ChatroomResponseDTO> getAllChatrooms() {
        List<Chatroom> chatroomList = chatroomRepository.findAllActivated();

        return chatroomList.stream().map(ChatroomResponseDTO::new).toList();
    }

    public ChatroomResponseDTO createChatroom(ChatRoomRequestDTO request, User user) {
        String title = request.getTitle();
        String category = request.getCategory();
        UUID userId = user.getId();
        Boolean isPrivate = request.getIsPrivate();
        String password = null;

        if(isPrivate) {
            String inputPassword = request.getPassword();

            if(inputPassword == null || inputPassword.isEmpty()) {
                throw new IllegalArgumentException("비공개 대화방은 비밀번호를 입력해야합니다.");
            } else {
                password = passwordEncoder.encode(inputPassword);
            }
        }

        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .category(category)
                .status(ChatroomStatus.ACTIVATED)
                .isPrivate(isPrivate)
                .password(password)
                .createdBy(userId)
                .build();

        return new ChatroomResponseDTO(chatroom);
    }

    @Transactional
    public void deleteChatroom(UUID chatroomId, User user) {
        Chatroom chatroom = chatroomRepository.findActivatedRoomById(chatroomId).orElseThrow(
                ()-> new IllegalArgumentException("해당 대화방은 없습니다.")
        );

        if(!chatroom.getCreatedBy().equals(user.getId())) {
            throw new IllegalArgumentException("방장만 대화방을 삭제할 수 있습니다.");
        }

        chatroom.updateStatus(ChatroomStatus.DEACTIVATED);

        List<ChatroomMember> members = chatroomMemberRepository.findAllByChatroomId(chatroomId);

        LocalDateTime now = LocalDateTime.now();

        for (ChatroomMember member : members) {
            member.updateIsJoined(false);
            member.updateLeftAt(now);
        }
    }
}
