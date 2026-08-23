package org.personal.washingmachine.usecase.user.registeruser;

import org.personal.washingmachine.entity.User;

class UserTestData {
	static User createUser() {
		return new User(
				"RX1001",
				"Bosch",
				"Poland",
				"unique@email.com",
				"unique_username",
				"somePassword"
		);
	}

	static RegisterUserRequest createUserRequest() {
		return new RegisterUserRequest(
				"RX1001",
				"Bosch",
				"Poland",
				"unique@email.com",
				"unique_username",
				"somePassword"
		);
	}
}
