package org.personal.washingmachine.dto;

public record LoginUserResponse(
        String code,
        String organization,
        String country,
        String email,

        String username
) { }