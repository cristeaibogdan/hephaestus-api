package org.personal.washingmachine;

import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.entity.User;

public class UserTestData {
	public static User createUser() {
		return new User(
				"RX1001",
				"Bosch",
				"Poland",
				"unique@email.com",
				"unique_username",
				"somePassword"
		);
	}

	public static CreateUserRequest createUserRequest() {
		return new CreateUserRequest(
				"RX1001",
				"Bosch",
				"Poland",
				"unique@email.com",
				"unique_username",
				"somePassword"
		);
	}
}
