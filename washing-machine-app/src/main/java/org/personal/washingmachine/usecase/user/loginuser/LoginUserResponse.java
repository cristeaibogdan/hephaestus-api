package org.personal.washingmachine.usecase.user.loginuser;

record LoginUserResponse(
        String code,
        String organization,
        String country,
        String email,

        String username
) { }