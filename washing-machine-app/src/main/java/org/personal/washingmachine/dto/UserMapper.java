package org.personal.washingmachine.dto;

import org.personal.washingmachine.entity.User;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper {

	public User toEntity(CreateUserRequest dto) {
		return new User(
				dto.code(),
				dto.organization(),
				dto.country(),
				dto.email(),
				dto.username(),
				dto.password()
		);
	}

	public LoginUserResponse toLoginUserResponse(User entity) {
		return new LoginUserResponse(
				entity.getCode(),
				entity.getOrganization(),
				entity.getCountry(),
				entity.getEmail(),
				entity.getUsername()
		);
	}
}
