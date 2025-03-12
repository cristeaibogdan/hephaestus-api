package org.personal.solarpanel;

import org.personal.solarpanel.dto.SaveSolarPanelDamageRequest;
import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;

public class TestData {

	public static SaveSolarPanelRequest createValidSaveSolarPanelRequest() {
		return new SaveSolarPanelRequest(
				"Solar Panel",
				"defaultManufacturer",
				"defaultModel",
				"defaultType",
				"defaultSeriaL",
				new SaveSolarPanelDamageRequest(
						true, // so no recommendation exception is thrown
						false,
						false,
						false,
						"blue paint was thrown on half the panel"
				)
		);
	}

	public static SolarPanel createValidSolarPanel(String serialNumber) {
		return new SolarPanel(
				"Solar Panel",
				"Manufacturer",
				"model",
				"type",
				serialNumber,
				new Damage(
						true, // to avoid recommendation exception
						false,
						false,
						false,
						""
				)
		);
	}
}
