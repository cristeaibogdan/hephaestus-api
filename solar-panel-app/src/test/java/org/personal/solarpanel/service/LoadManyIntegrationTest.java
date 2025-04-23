package org.personal.solarpanel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.dto.GetSolarPanelFullResponse;
import org.personal.solarpanel.entity.Damage;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
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
		void should_ReturnDTO_With_CorrectProperties() {
			// GIVEN
			saveToDB(
					new SolarPanel(
							"Solar Panel",
							"Manufacturer",
							"model",
							"type",
							"serial1",
							new Damage(
									true, // to avoid recommendation exception
									false,
									false,
									false,
									"some description"
							)
					)
			);

			GetSolarPanelFullResponse expected = new GetSolarPanelFullResponse(
					"Solar Panel",
					"Manufacturer",
					"model",
					"type",
					"serial1",
					LocalDateTime.now(),
					Recommendation.REPAIR,
					new GetSolarPanelFullResponse.DamageResponse(
							true,
							false,
							false,
							false,
							"some description"
					)
			);

			// WHEN
			Map<String, GetSolarPanelFullResponse> actual = underTest.loadMany(Set.of("serial1"));

			// THEN
			assertThat(actual.get("serial1"))
					.usingRecursiveComparison()
					.ignoringFields("createdAt")
					.isEqualTo(expected);
		}

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

