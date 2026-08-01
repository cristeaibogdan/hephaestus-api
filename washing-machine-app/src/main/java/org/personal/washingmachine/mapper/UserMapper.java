package org.personal.washingmachine.mapper;

import org.personal.washingmachine.dto.CreateUserRequest;
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

}
