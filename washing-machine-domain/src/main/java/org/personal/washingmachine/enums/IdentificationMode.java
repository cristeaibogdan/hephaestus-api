package org.personal.washingmachine.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum IdentificationMode {
	QR_CODE ("QR Code"),
	DATA_MATRIX ("Data Matrix");

	private final String label;

	IdentificationMode(String label) {
		this.label = label;
	}

	@JsonValue // Allows value received from frontend to be mapped to this enum. Ex.: Frontend sends Service and gets mapped to SERVICE
	public String getLabel() {
		return label;
	}
}
