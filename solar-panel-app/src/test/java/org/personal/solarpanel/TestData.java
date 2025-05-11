package org.personal.solarpanel;

import org.personal.solarpanel.dto.SaveSolarPanelRequest;
import org.personal.solarpanel.dto.SearchSolarPanelRequest;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;

public class TestData {

	public static SaveSolarPanelRequest createValidSaveSolarPanelRequest() {
		return new SaveSolarPanelRequest(
				"Solar Panel",
				"defaultManufacturer",
				"defaultModel",
				"defaultType",
				"defaultSeriaL",
				new SaveSolarPanelRequest.Damage(
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

	public static Damage createDamageWithRecommendation(Recommendation expected) {
		return switch (expected) {
			case REPAIR -> createDamage()
					.setBrokenGlass(true);
			case RECYCLE -> createDamage()
					.setBrokenGlass(true)
					.setMicroCracks(true)
					.setSnailTrails(true);
			case DISPOSE -> createDamage()
					.setBrokenGlass(true)
					.setMicroCracks(true)
					.setSnailTrails(true)
					.setHotSpots(true);
		};
	}

	private static Damage createDamage() {
		return new Damage(
				false,
				false,
				false,
				false,
				""
		);
	}

	public static SearchSolarPanelRequest createSearchSolarPanelRequest() {
		return new SearchSolarPanelRequest(
				0,
				2,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		);
	}
}
