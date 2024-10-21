package org.personal.washingmachine.dto;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String code,
        String organization,
        String country,
        String email,

        String username,
        String password
) { }