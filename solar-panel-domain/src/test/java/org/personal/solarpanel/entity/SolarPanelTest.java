package org.personal.solarpanel.entity;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;

class SolarPanelTest {

	private Damage validDamage = new Damage(
			true,
			false,
			false,
			false,
			""
	);
	// TODO: consider moving to a TestData object mother
	private SolarPanel validSolarPanel = new SolarPanel(
			"Solar Panel",
			"Manufacturer",
			"model1",
			"type1",
			"serialNumber",
			this.validDamage
	);


	@Test
	void should_ReturnRecommendationRepair_When_OneDamageFlagIsTrue() {
		// GIVEN
		SolarPanel solarPanel = validSolarPanel.setDamage(
				validDamage.setHotSpots(true)
		);
		// WHEN
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.REPAIR);
	}

	@Test
	void should_ReturnRecommendationDispose_When_AllDamageFlagsAreTrue() {
		// GIVEN
		SolarPanel solarPanel = validSolarPanel.setDamage(
				validDamage.setHotSpots(true)
						.setBrokenGlass(true)
						.setMicroCracks(true)
						.setSnailTrails(true)
		);
		// WHEN
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.DISPOSE);
	}

	@Test
	void should_ReturnRecommendationRecycle_When_DamageIsModifiedWithSetter() {
		// GIVEN
		SolarPanel solarPanel = validSolarPanel.setDamage(
				validDamage.setHotSpots(true) // will return Repair recommendation
		);
		// WHEN
		solarPanel.setDamage(
				validDamage.setHotSpots(true)
						.setSnailTrails(true)
						.setMicroCracks(true)
		);
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.RECYCLE);
	}

}