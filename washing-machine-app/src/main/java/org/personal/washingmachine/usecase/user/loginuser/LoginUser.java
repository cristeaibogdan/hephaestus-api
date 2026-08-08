package org.personal.washingmachine.usecase.user.loginuser;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class LoginUser {
	private final UserService service;

	@PostMapping("/v1/users/login")
	LoginUserResponse handle(@RequestBody LoginUserRequest loginUserRequest) {
		User user = service.login(loginUserRequest.username(), loginUserRequest.password());
		return toLoginUserResponse(user);
	}

	private LoginUserResponse toLoginUserResponse(User entity) {
		return new LoginUserResponse(
				entity.getCode(),
				entity.getOrganization(),
				entity.getCountry(),
				entity.getEmail(),
				entity.getUsername()
		);
	}
}
