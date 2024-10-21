package org.personal.washingmachine.dto;

import lombok.Builder;

@Builder
public record LoginUserResponse(
        String code,
        String organization,
        String country,
        String email,

        String username
) { }