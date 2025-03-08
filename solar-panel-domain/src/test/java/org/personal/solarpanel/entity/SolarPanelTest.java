package org.personal.solarpanel.entity;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.enums.Recommendation;

import static org.assertj.core.api.Assertions.assertThat;

class SolarPanelTest {

	@Test
	void should_ReturnRecommendationRepair_When_OneDamageFlagIsTrue() {
		// GIVEN
		SolarPanel solarPanel = new SolarPanel(
				"Solar Panel",
				"Manufacturer",
				"model1",
				"type1",
				"serialNumber",
				new Damage(
						true,
						false,
						false,
						false,
						""
				)
		);
		// WHEN
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.REPAIR);
	}

	@Test
	void should_ReturnRecommendationDispose_When_AllDamageFlagsAreTrue() {
		// GIVEN
		SolarPanel solarPanel = new SolarPanel(
				"Solar Panel",
				"Manufacturer",
				"model1",
				"type1",
				"serialNumber",
				new Damage(
						true,
						true,
						true,
						true,
						""
				)
		);
		// WHEN
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.DISPOSE);
	}

	@Test
	void should_ReturnRecommendationRecycle_When_DamageIsModifiedWithSetter() {
		// GIVEN
		SolarPanel solarPanel = new SolarPanel(
				"Solar Panel",
				"Manufacturer",
				"model1",
				"type1",
				"serialNumber",
				new Damage(
						true, // will have recommendation REPAIR
						false,
						false,
						false,
						""
				)
		);
		// WHEN
		solarPanel.setDamage(new Damage(
				true,
				true,
				true,
				false,
				""
		));
		Recommendation actual = solarPanel.getRecommendation();

		// THEN
		assertThat(actual).isEqualTo(Recommendation.RECYCLE);
	}

}