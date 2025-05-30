package com.example.toytalk.domain.chat.controller;

import com.example.toytalk.domain.chat.dto.ChatErrorResponseDTO;
import com.example.toytalk.domain.chat.dto.ChatRequestDTO;
import com.example.toytalk.domain.chat.dto.ChatResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat/{chatroomId}")
    @SendTo("/subscribe/chat/{chatroomId}")
    public ChatResponseDTO sendMessage(ChatRequestDTO request,
                                       @DestinationVariable String chatroomId) {
        return new ChatResponseDTO(request.userId(), request.content(), request.sentAt());
    }

    @MessageExceptionHandler
    public ChatErrorResponseDTO messageExceptionHandler(Exception ex) {
        return new ChatErrorResponseDTO("ERROR", ex.getMessage());
    }
}
