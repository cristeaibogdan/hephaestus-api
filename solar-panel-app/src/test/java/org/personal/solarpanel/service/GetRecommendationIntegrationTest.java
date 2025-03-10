package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.enums.Recommendation;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetRecommendationIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Nested
	class IntegrationTest {

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
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnStatusOk_When_SerialNumberFound() throws Exception {
			// GIVEN
			insertIntoDB(TestData.createValidSolarPanel("status OK"));

			// WHEN
			ResultActions resultActions = performRequest("status OK");

			// THEN
			resultActions
					.andExpect(status().isOk());
		}

		@Test
		void should_ThrowException_When_SerialNumberNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("not found");

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

	}

	private ResultActions performRequest(String serialNumber) throws Exception {
		return mockMvc.perform(
				get("/v1/solar-panels/{serialNumber}/recommendation", serialNumber)
		);
	}

	private void insertIntoDB(SolarPanel... solarPanels){
		repository.saveAll(List.of(solarPanels));
	}
}
