package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoadManyIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

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
					new GetSolarPanelFullResponse.Damage(
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
			saveToDB(
					TestData.createValidSolarPanel("serial1")
			);
			// WHEN
			Map<String, GetSolarPanelFullResponse> actual = underTest.loadMany(Set.of("I don't exist", "Nothing", "serial1"));

			// THEN
			assertThat(actual)
					.containsOnlyKeys("I don't exist", "Nothing", "serial1")
					.containsEntry("I don't exist", null)
					.containsEntry("Nothing", null);
		}
	}

	@Nested
	class MvcTest {
		@Test
		void should_ReturnStatusOk_When_SerialNumbersFound() throws Exception {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("serialA"),
					TestData.createValidSolarPanel("serialB")
			);

			// WHEN
			ResultActions resultActions = performRequest(Set.of("serialA", "serialB"));

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumbersNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(Set.of("I do not exist", "Can't find me!"));

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))))
					.andExpect(content().string(containsString("I do not exist")))
					.andExpect(content().string(containsString("Can't find me!")));
		}
	}

	private void saveToDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}

	private ResultActions performRequest(Set<String> request) throws Exception {
		return mockMvc.perform(
				post("/v1/solar-panels/many")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}

