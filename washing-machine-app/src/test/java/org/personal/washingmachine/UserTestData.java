package org.personal.washingmachine;

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
}
