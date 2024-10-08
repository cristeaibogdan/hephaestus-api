package org.personal.washingmachine.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        String code,
        String organization,
        String country,
        String email,

        String username,
        String password
) {
}
