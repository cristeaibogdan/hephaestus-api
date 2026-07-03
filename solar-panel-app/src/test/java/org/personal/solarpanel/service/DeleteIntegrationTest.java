package org.personal.solarpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.solarpanel.BaseIntegrationTest;
import org.personal.solarpanel.TestData;
import org.personal.solarpanel.entity.SolarPanel;
import org.personal.solarpanel.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteIntegrationTest extends BaseIntegrationTest {

	@Autowired SolarPanelApplicationService underTest;
	@Autowired SolarPanelRepository repository;

	@Autowired MockMvc mockMvc;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	private void saveToDB(SolarPanel... solarPanels) {
		repository.saveAll(List.of(solarPanels));
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_deleteSolarPanel_When_SerialNumberIsFound() {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("Delete me!")
			);

			// WHEN
			underTest.delete("Delete me!");

			// THEN
			assertThat(repository.findBySerialNumber("Delete me!")).isEmpty();
		}

	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnExpectedContent_When_SerialNumberFound() throws Exception {
			// GIVEN
			saveToDB(
					TestData.createValidSolarPanel("I'll be deleted from the DB")
			);

			// WHEN
			ResultActions resultActions = performRequest("I'll be deleted from the DB");

			// THEN
			resultActions
					.andExpect(status().isNoContent())
					.andExpect(content().string(emptyString()));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumberNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I don't exist");

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		private ResultActions performRequest(String serialNumber) throws Exception {
			return mockMvc.perform(
					delete("/v1/solar-panels/{serialNumber}", serialNumber)
			);
		}
	}

}
