package org.personal.washingmachine.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CreateUserRequest(
        String code,
        String organization,
        String country,
        String email,

        String username,
        String password
) { }