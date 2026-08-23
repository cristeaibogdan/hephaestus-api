package org.personal.washingmachine.usecase.user.loginuser;

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
}
