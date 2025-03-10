package org.personal.solarpanel.service;

import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetRecommendationIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Test
	void should_ReturnRecommendation_When_SerialNumberFound() {
		// GIVEN
		insertIntoDB(TestData.createValidSolarPanel("serialNumber")
				.setDamage(
						TestData.createDamageWithRecommendation(Recommendation.REPAIR)
				)
		);

		// WHEN
		Recommendation actual = underTest.getRecommendation("serialNumber");

		// THEN
		assertThat(actual).isEqualTo(Recommendation.REPAIR);
	}

	private void insertIntoDB(SolarPanel... solarPanels){
		repository.saveAll(List.of(solarPanels));
	}
}
