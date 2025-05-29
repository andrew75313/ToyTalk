package com.example.toytalk.domain.chatroom.controller;

import com.example.toytalk.domain.chatroom.dto.ChatRoomRequestDTO;
import com.example.toytalk.domain.chatroom.dto.ChatroomResponseDTO;
import com.example.toytalk.domain.chatroom.dto.EnterChatroomRequestDTO;
import com.example.toytalk.domain.chatroom.service.ChatroomService;
import com.example.toytalk.global.security.user.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @GetMapping
    public ResponseEntity<List<ChatroomResponseDTO>> getAllChatrooms() {
        List<ChatroomResponseDTO> chatrooms = chatroomService.getAllChatrooms();
        return ResponseEntity.status(HttpStatus.OK).body(chatrooms);
    }

    @PostMapping
    public ResponseEntity<ChatroomResponseDTO> createChatroom(@Valid @RequestBody ChatRoomRequestDTO request,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatroomResponseDTO createdChatroom = chatroomService.createChatroom(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChatroom);
    }

    @DeleteMapping("/{chatroomId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable String chatroomId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatroomService.deleteChatroom(UUID.fromString(chatroomId), userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{chatroomId}/enter")
    public ResponseEntity<Void> enterChatroom(
            @PathVariable String chatroomId,
            @RequestBody EnterChatroomRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean verified = chatroomService.enterChatroom(UUID.fromString(chatroomId), request.password(), userDetails.getUser());

        if (!verified) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @PostMapping("/{chatroomId}/exit")
    public ResponseEntity<Void> exitChatroom(
            @PathVariable String chatroomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean exited = chatroomService.exitChatroom(UUID.fromString(chatroomId), userDetails.getUser());

        if (!exited) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
