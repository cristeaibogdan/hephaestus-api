package org.personal.washingmachine.usecase.user.registeruser;

import lombok.With;

@With
record RegisterUserRequest(
        String code,
        String organization,
        String country,
        String email,

        String username,
        String password
) { }