package com.example.toytalk.domain.users.entity;

public enum UserStatus {
    ACTIVATED("ACTIVATED"),
    DEACTIVATED("DEACTIVATED");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
