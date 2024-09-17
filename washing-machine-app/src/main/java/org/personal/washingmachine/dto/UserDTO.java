package org.personal.washingmachine.dto;

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

	public UserDTO(User entity) {
		this(
				entity.getCode(),
				entity.getOrganization(),
				entity.getCountry(),
				entity.getEmail(),
				entity.getUsername(),
				null
		);
	}

	public User toEntity() { //TODO: How to show what parameter is which constructor??
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
