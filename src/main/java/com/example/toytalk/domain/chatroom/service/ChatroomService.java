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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ChatroomResponseDTO> getAllChatrooms(int page) {
        int pageSize = 10;
        int pageNumber = page > 1 ? page - 1 : 0;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Chatroom> chatRoomPage = chatroomRepository.findAllActivated(pageable);

        return chatRoomPage.stream()
                .map(chatroom -> {
                    long memberCount = chatroomMemberRepository.countAllJoinedMember(chatroom.getId());
                    ChatroomResponseDTO dto = new ChatroomResponseDTO(chatroom);
                    dto.setMemberCount(memberCount);
                    return dto;
                })
                .toList();
    }


    public ChatroomResponseDTO getChatroom(UUID chatroomId) {
        Chatroom chatroom = chatroomRepository.findActivatedRoomById(chatroomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 대화방입니다.")
        );

        Long memberCount = chatroomMemberRepository.countAllJoinedMember(chatroomId);

        ChatroomResponseDTO chatroomResponseDTO = new ChatroomResponseDTO(chatroom);
        chatroomResponseDTO.setMemberCount(memberCount);

        return chatroomResponseDTO;
    }

    public ChatroomResponseDTO createChatroom(ChatRoomRequestDTO request, User user) {
        String title = request.getTitle();
        String category = request.getCategory();
        UUID userId = user.getId();
        Boolean isPrivate = request.getIsPrivate();
        String password = null;

        if (isPrivate) {
            String inputPassword = request.getPassword();

            if (inputPassword == null || inputPassword.isEmpty()) {
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
                () -> new IllegalArgumentException("해당 대화방은 없습니다.")
        );

        if (!chatroom.getCreatedBy().equals(user.getId())) {
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

    public boolean enterChatroom(UUID chatroomId, String password, User user) {
        Chatroom chatroom = chatroomRepository.findActivatedRoomById(chatroomId).orElse(null);

        if (chatroom == null) {
            return false;
        }

        if (!passwordEncoder.matches(chatroom.getPassword(), password)) {
            return false;
        }

        ChatroomMember member = chatroomMemberRepository.findJoinedMemberInChatroom(chatroomId, user.getId())
                .orElse(null);

        if (member == null) {
            ChatroomMember chatroomMember = ChatroomMember.builder()
                    .chatroomId(chatroomId)
                    .userId(user.getId())
                    .joinedAt(LocalDateTime.now())
                    .isJoined(true)
                    .build();

            chatroomMemberRepository.save(chatroomMember);
        }

        return true;
    }

    @Transactional
    public boolean exitChatroom(UUID chatroomId, User user) {
        ChatroomMember member = chatroomMemberRepository.findJoinedMemberInChatroom(chatroomId, user.getId())
                .orElse(null);

        if (member == null) {
            return false;
        }

        member.updateLeftAt(LocalDateTime.now());
        member.updateIsJoined(false);

        return true;
    }
}
