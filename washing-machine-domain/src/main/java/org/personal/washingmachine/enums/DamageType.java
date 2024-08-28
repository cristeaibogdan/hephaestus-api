package org.personal.washingmachine.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DamageType {
	IN_USE ("In Use"),
	IN_TRANSIT ("In Transit");

	private final String label;

	DamageType(String label) {
		this.label = label;
	}

	@JsonValue // Allows value received from frontend to be mapped to this enum. Ex.: Frontend sends Service and gets mapped to SERVICE
	public String getLabel() {
		return label;
	}
}
