package com.example.toytalk.domain.users.entity;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString(){
        return authority;
    }

    public String getAuthority() {
        return authority;
    }
}
