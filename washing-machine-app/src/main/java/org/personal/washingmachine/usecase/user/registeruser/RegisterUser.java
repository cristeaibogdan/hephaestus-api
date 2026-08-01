package org.personal.washingmachine.usecase.user.registeruser;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class RegisterUser { // TODO: Replace with proper authentication
	private final UserService service;

	@PostMapping("/api/v1/users/register")
	@ResponseStatus(HttpStatus.CREATED)
	void register(@RequestBody RegisterUserRequest registerUserRequest) {
		User user = toEntity(registerUserRequest);
		service.register(user);
	}

	private User toEntity(RegisterUserRequest dto) {
		return new User(
				dto.code(),
				dto.organization(),
				dto.country(),
				dto.email(),
				dto.username(),
				dto.password()
		);
	}
}
