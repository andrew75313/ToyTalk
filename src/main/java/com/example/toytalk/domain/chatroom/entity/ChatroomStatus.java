package com.example.toytalk.domain.chatroom.entity;

public enum ChatroomStatus {
    ACTIVATED("ACTIVATED"),
    DEACTIVATED("DEACTIVATED");

    private final String status;

    ChatroomStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
