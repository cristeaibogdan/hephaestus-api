package org.personal.washingmachine.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReturnType {
	SERVICE ("Service"),
	COMMERCIAL ("Commercial"),
	TRANSPORT ("Transport");

	private final String label;

	ReturnType(String label) {
		this.label = label;
	}

	@JsonValue // Allows value received from frontend to be mapped to this enum. Ex.: Frontend sends Service and gets mapped to SERVICE
	public String getLabel() {
		return label;
	}
}
