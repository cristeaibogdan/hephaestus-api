package org.personal.washingmachine.dto;

public record LoginUserRequest(
        String username,
        String password
) {}
