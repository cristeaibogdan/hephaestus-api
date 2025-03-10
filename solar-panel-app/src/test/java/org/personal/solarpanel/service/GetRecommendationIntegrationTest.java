package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetRecommendationIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@ParameterizedTest(name = "Should return recommendation = {0}")
	@EnumSource(Recommendation.class)
	void should_ReturnRecommendation_When_SerialNumberFound(Recommendation expected) {
		// GIVEN
		insertIntoDB(TestData.createValidSolarPanel("serialNumber")
				.setDamage(
						TestData.createDamageWithRecommendation(expected)
				)
		);

		// WHEN
		Recommendation actual = underTest.getRecommendation("serialNumber");

		// THEN
		assertThat(actual).isEqualTo(expected);
	}

	private void insertIntoDB(SolarPanel... solarPanels){
		repository.saveAll(List.of(solarPanels));
	}
}
