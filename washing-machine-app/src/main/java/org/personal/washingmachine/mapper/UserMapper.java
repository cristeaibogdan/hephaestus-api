package org.personal.washingmachine.mapper;

import org.personal.washingmachine.dto.UserDTO;
import org.personal.washingmachine.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public User toEntity(UserDTO dto) {
		return new User(
				dto.code(),
				dto.organization(),
				dto.country(),
				dto.email(),
				dto.username(),
				dto.password()
		);
	}

	public UserDTO toDTO(User entity) {
		return new UserDTO(
				entity.getCode(),
				entity.getOrganization(),
				entity.getCountry(),
				entity.getEmail(),
				entity.getUsername(),
				null
		);
	}
}
