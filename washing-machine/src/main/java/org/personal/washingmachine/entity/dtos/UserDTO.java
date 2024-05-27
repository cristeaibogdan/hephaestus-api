package org.personal.washingmachine.entity.dtos;

import lombok.Builder;
import org.personal.washingmachine.entity.User;

@Builder
public record UserDTO(
        String code,
        String organization,
        String country,
        String email,

        String username,
        String password
) {
    public User toUser() {
        return new User(
                this.code,
                this.organization,
                this.country,
                this.email,
                this.username,
                this.password
        );
    }
}
