package org.personal.solarpanel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.GetSolarPanelFullResponse;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoadManyIntegrationTest extends BaseIntegrationTest {

	@Autowired
	SolarPanelApplicationService underTest;
	@Autowired
	SolarPanelRepository repository;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {
		@Test
		void should_ReturnDTOs_When_SerialNumbersFound() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serial1"),
					TestData.createValidSolarPanel("serial2"),
					TestData.createValidSolarPanel("serial3")
			);

			// WHEN
			Map<String, GetSolarPanelFullResponse> actual = underTest.loadMany(Set.of("serial1", "serial2"));

			// THEN
			assertThat(actual).containsOnlyKeys("serial1", "serial2");

			assertThat(actual.values())
					.extracting(wm -> wm.serialNumber())
					.containsOnly("serial1", "serial2");
		}

		@Test
		void should_ReturnNullDTOs_When_SerialNumbersNotFound() {
			// GIVEN
			// WHEN
			Map<String, GetSolarPanelFullResponse> actual = underTest.loadMany(Set.of("I don't exist", "Nothing"));

			// THEN
			assertThat(actual)
					.containsOnlyKeys("I don't exist", "Nothing")
					.containsEntry("I don't exist", null)
					.containsEntry("Nothing", null);
		}
	}

	private void saveToDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}
}

