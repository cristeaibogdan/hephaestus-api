package org.personal.solarpanel;

import org.personal.solarpanel.dto.SaveSolarPanelDamageRequest;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;

public class TestData {

	public static SaveSolarPanelRequest createSaveSolarPanelRequest() {
		return new SaveSolarPanelRequest(
				"Solar Panel",
				"defaultManufacturer",
				"defaultModel",
				"defaultType",
				"defaultSeriaL",
				new SaveSolarPanelDamageRequest(
						false,
						false,
						false,
						false,
						"blue paint was thrown on half the panel"
				)
		);
	}
}
