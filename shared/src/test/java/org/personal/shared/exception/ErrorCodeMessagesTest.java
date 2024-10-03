package org.personal.shared.exception;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ResourceBundle;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * <p> This class tests that every key in {@link org.personal.shared.exception.ErrorCode} is present in all files of type: messages_*locale*.properties
 */

class ErrorCodeMessagesTest {

	@ParameterizedTest
	@ValueSource(strings = {
			"messages",
			"messages_es",
			"messages_it",
			"messages_ro",
			"messages_sl"
	})
	public void should_Pass_When_AllEnumKeysArePresentInMessagesFiles(String bundleName) {
		// GIVEN
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

		// WHEN
		for (ErrorCode errorCode : ErrorCode.values()) {
			String errorCodeKey = errorCode.name();

			// THEN
			assertThat(resourceBundle.containsKey(errorCodeKey))
					.as("Key " + errorCodeKey + " is missing in " + bundleName + ".properties")
					.isTrue();
		}
	}
}